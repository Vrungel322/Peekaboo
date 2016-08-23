package com.peekaboo.messaging.socket.worker

import java.io.File
import java.nio.file.{Path, Paths}
import java.util.UUID

import akka.actor.Actor
import com.peekaboo.messaging.socket.middleware.BinaryMessageInterceptor
import com.peekaboo.transformation.{AudioToTextInterface, AudioToTextWatson, SpeechRecognitionOptions}
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
    case (msg: Send,destinationId: String) =>
        val converter: AudioToTextInterface = new AudioToTextWatson
        val rootPath: String = System.getProperty("catalina.home")
        val rootDir = new File(rootPath + File.separator + "tmp")
//        logger.error(new String(msg.getBody, "UTF-8"))
        val filename = new String(msg.getBody, "UTF-8")
        val file: Path = java.nio.file.Paths.get(rootDir.getAbsolutePath,destinationId, filename)
      logger.debug("Target file:"+file)

      try {
        val textConverted = converter.RunServiceWithDefaults(file.toFile)
        logger.error(textConverted)
        //nice place to add conversion
        //have to check user's state
        //and than if needed perform some actions
        //val audiomsg = new Send(textConverted.getBytes("UTF-8"), msg.parameters)
        //val action = audiomsg.toMessage(msg.getSender)
        val m=new SendText(textConverted,msg.parameters)

        val action = m.toMessage(destinationId)
        sendMessage(action)
      }
      catch {
        case ex: Exception =>
          logger.error(ex.getMessage)
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
