package com.peekaboo.messaging.socket.worker

import java.io.{File, FileOutputStream, InputStream}
import java.nio.file.Path
import java.util.UUID

import akka.actor.Actor
import com.peekaboo.messaging.socket.middleware.BinaryMessageInterceptor
import com.peekaboo.miscellaneous.JavaPropertiesParser
import com.peekaboo.model.Neo4jSessionFactory
import com.peekaboo.model.entity.Storage
import com.peekaboo.model.entity.enums.{FileType, UserState}
import com.peekaboo.model.repository.impl.{StorageRepositoryImpl, UserRepositoryImpl}
import com.peekaboo.transformation._
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.LogManager
import org.springframework.web.socket.{BinaryMessage, WebSocketSession}

class MessageActor(private val socket: WebSocketSession) extends Actor {
  //small problem
  //probably we can't add state to the actor
  //so it would be great to find the way how to store User's state (receive audio, receive text, receive any)

  //  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {

    // Here might be a possibility that connection with socket wasn't closed due to some errors on client side.
    // Have to do it manually
    logger.debug("Check if socket is open")
    if (socket.isOpen) {
      logger.debug("Closing connection with socket")
      socket.close()
    } else {
      logger.debug("Socket is closed. Nothing to do here")
    }
  }

  def receive = {
    case (msg: Message, sender: String, destination: String, messageType: String) =>

      logger.error("Got to message Actor")
      try {
        //here we are searching the state of destination user
        val userRepository = new UserRepositoryImpl(new Neo4jSessionFactory)
        val user = userRepository.findById(destination.toLong)
        val state = user.getState
        logger.error("state:" + state)
        //here userstates are defined - they will be used a lot of times
        val typeText = UserState.TEXT.getName
        val stateText = UserState.TEXT.getId
        val typeAudio = UserState.AUDIO.getName
        val stateAudio = UserState.AUDIO.getId
        //then actions of actor are defined, depending on type of message and status of user


        //***************************TEXT--------->>TEXT************************************
        if ((messageType == UserState.TEXT.getName) && (state == UserState.TEXT.getId)) {
          logger.error("processing text2text")
          val action = msg.toMessage(sender)
          sendMessage(action)
        }
        //***************************AUDIO--------->>TEXT************************************
        if ((messageType == UserState.AUDIO.getName) && (state == UserState.TEXT.getId)) {
          logger.error("processing audio to text")
          val converter: AudioToTextInterface = new AudioToTextYandex //here is our converter
          //next routine is searching the path of file which were stored using http "upload"
          //todo:rewrite catalina home to build paths in one place
          val rootPath: String = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"))
          val rootDir = new File(rootPath + File.separator + "tmp")
          //        logger.error(new String(msg.getBody, "UTF-8"))
          val filename = new String(msg.getBody, "UTF-8")
          val audiofolder=JavaPropertiesParser.PARSER.getValue("Audio")
          val file: Path = java.nio.file.Paths.get(rootDir.getAbsolutePath, destination, audiofolder,  filename)
          logger.error("Target file:" + file)

          val messageText :String = converter.RunServiceWithDefaults(file.toFile) //converting audio to txt using watson
          logger.error("converted")
          val parameters = msg.parameters
          parameters + ("type" -> "text")
          val message = new SendText(messageText, parameters)
          logger.error("Converted message text:" + messageText)
          val action = message.toMessage(sender)
          sendMessage(action)
        }

        //***************************TEXT--------->>AUDIO************************************

        if ((messageType == UserState.TEXT.getName) && (state == UserState.AUDIO.getId)) {
          try {
            logger.error("processing text2audio")
            val messageText = new String(msg.getBody, "UTF-8")
            val audiofolder=JavaPropertiesParser.PARSER.getValue("Audio")
            val rootPath = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"))
            //todo:we save converted audio to destination folder, but is it right?  Also Timofei have to add creation of file
            val rootDir = new File(rootPath + File.separator + "tmp" + File.separator + destination+ File.separator+audiofolder)
            if (!rootDir.exists) rootDir.mkdirs()
            val fileName: String = UUID.randomUUID.toString

            val uploadedFile = new File(rootDir.getAbsolutePath + File.separator + fileName + ".tmp")
            logger.error(uploadedFile.getPath)
            uploadedFile.createNewFile
            val o = new FileOutputStream(uploadedFile)
            val buffer: Array[Byte] = new Array[Byte](1024)
            val t = new TextToAudioYandex
            val in: InputStream = t.RunServiceWithDefaults(messageText)
            IOUtils.copy(in, o)
            o.flush()
            o.close()
            in.close()


            //Test wav To mp3 Convertion
            val target: File = new File(rootDir.getAbsolutePath + File.separator + fileName)
            //          logger.error(target)
            //          logger.error(uploadedFile)
            //          logger.error("we are before future")
            //           val hello=new Thread(new Runnable {
            //             def run(): Unit = {
            //
            //               val audioAttr = new AudioAttributes
            //               val mimeType: String = "audio/mp3"
            //
            //               val aed: AudioConverter = new AudioConverter()
            //
            //                aed.encodeAudio(uploadedFile, target, mimeType)
            //             }
            //           })
            //          hello.start()
            val headerFixer = new WavHeaderFixer(uploadedFile, target)
            headerFixer.fix()
            uploadedFile.delete()
            val storage: Storage = new Storage(fileName, target.getAbsolutePath, FileType.AUDIO.`type`())
            val storageService = new StorageRepositoryImpl(new Neo4jSessionFactory)

            storageService.save(storage)
            user.getOwnStorages.add(storage)
            userRepository.save(user)
            var parameters: scala.Predef.Map[String, String] = Map()
            parameters += ("type" -> "audio")

            val message = new Message(fileName.getBytes, parameters)
            val action = message.toMessage(sender)
            sendMessage(action)
          } catch {
            case a: Error => logger.error(a.printStackTrace())
          }
        }

        //***************************AUDIO--------->>AUDIO************************************
        if (messageType == UserState.AUDIO.getName && state == UserState.AUDIO.getId) {
          logger.error("Send audio to audio")
          val action = msg.toMessage(sender)
          sendMessage(action)
        }
        //***************************IMAGE-------->>TEXT********************************
        if (messageType == "image" && state == UserState.TEXT.getId) {
          logger.error("Send image to text")
          val rootPath: String = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"))
          val rootDir = new File(rootPath + File.separator + "tmp" + File.separator + destination)
          if (!rootDir.exists) rootDir.mkdirs()
          val fileName: String = new String(msg.getBody, "UTF-8")
          val imagefolder=JavaPropertiesParser.PARSER.getValue("Image")
          val file = new File(rootDir.getAbsolutePath + File.separator + imagefolder + File.separator + fileName)
          logger.error(file.getPath)
          val imageReader: ImageReader = new ImageReader(file)
          val messageText = imageReader.detect
          val parameters = msg.parameters
          parameters + ("type" -> "text")
          val message = new SendText(messageText, parameters)
          logger.debug("Converted message text:" + messageText)
          val action = message.toMessage(sender)
          sendMessage(action)
        }



        //******************************IMAGE->AUDIO***************************************
        if (messageType == "image" && state == UserState.AUDIO.getId) {
          logger.error("Send image to text")
          val rootPath: String = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"))
          val rootDir = new File(rootPath + File.separator + "tmp" + File.separator + destination)
          if (!rootDir.exists) rootDir.mkdirs()
          val fileName: String = new String(msg.getBody, "UTF-8")
          val imagefolder=JavaPropertiesParser.PARSER.getValue("Image")
          val file = new File(rootDir.getAbsolutePath + File.separator + imagefolder + File.separator + fileName)
          logger.error(file.getPath)
          val imageReader: ImageReader = new ImageReader(file)
          val messageText = imageReader.detect

          try {
            val rootPath = System.getProperty(JavaPropertiesParser.PARSER.getValue("FilesDestination"))
            //todo:we save converted audio to destination folder, but is it right?  Also Timofei have to add creation of file
            val rootDir = new File(rootPath + File.separator + "tmp" + File.separator + destination)
            if (!rootDir.exists) rootDir.mkdirs()
            val fileName: String = UUID.randomUUID.toString
            val audiofolder=JavaPropertiesParser.PARSER.getValue("Audio")
            val uploadedFile = new File(rootDir.getAbsolutePath+ File.separator + audiofolder + File.separator + fileName + ".tmp")
            logger.error(uploadedFile.getPath)
            uploadedFile.createNewFile
            val o = new FileOutputStream(uploadedFile)
            val buffer: Array[Byte] = new Array[Byte](1024)
            val t = new TextToAudioWatson
            val in: InputStream = t.RunServiceWithDefaults(messageText)
            IOUtils.copy(in, o)
            o.flush()
            o.close()
            in.close()


            //Test wav To mp3 Convertion
            val target: File = new File(rootDir.getAbsolutePath + File.separator + fileName)
            val headerFixer = new WavHeaderFixer(uploadedFile, target)
            headerFixer.fix()
            val storage: Storage = new Storage(fileName, target.getAbsolutePath, FileType.AUDIO.`type`())
            val storageService = new StorageRepositoryImpl(new Neo4jSessionFactory)

            storageService.save(storage)
            user.getOwnStorages.add(storage)
            userRepository.save(user)
            var parameters: scala.Predef.Map[String, String] = Map()
            parameters += ("type" -> "audio")

            val message = new Message(fileName.getBytes, parameters)
            val action = message.toMessage(sender)
            sendMessage(action)
          } catch {
            case a: Error => logger.error(a.toString)
          }
        }
        //*******************************Here is all other routines not connected with text or audio
        logger.error("UserState:" + UserState.ALL)
        if (state == UserState.ALL.getId) {
          logger.error("processing default routine for sending")
          val action = msg.toMessage(sender)
          sendMessage(action)
        }

      }

      catch {
        case e: Exception => logger.error(e.toString)
      }

    case (msg: SystemMessage, sender: String, destination: String, messageReason: String) =>
      logger.error("got to Actor")
      val reason = msg.getReason

      reason match {
        //        case Reason.Mode=>
        //          logger.error("got to REASON:MODE")
        //          val userRepository = new UserRepositoryImpl()
        //          logger.error(new String(msg.getBody))
        //          logger.debug(msg.getBody(0))
        //          val user=userRepository.findById(sender.toLong)
        //          user.setState(msg.getBody(0).toInt)
        //          userRepository.save(user)
        case Reason.Read =>
          logger.error("got to REASON:READ")
          val action = msg.toMessage(sender)
          sendMessage(action)
      }
    case a =>
      logger.debug("Message received but it cannot be resolved")
      logger.debug(a)
  }


  //sends binary message via socket
  private def sendMessage(message: Action): Unit = {
    val msg = BinaryMessageInterceptor.compose(message)
    logger.debug("Message:\n" + new String(msg))
    socket.sendMessage(new BinaryMessage(msg))
  }

  private val logger = LogManager.getLogger(MessageActor.this)
}
