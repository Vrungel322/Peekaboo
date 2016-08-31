package com.peekaboo.messaging.socket.worker

import java.io.{BufferedOutputStream, File, FileOutputStream}
import java.nio.file.{Files, Path}
import java.util.UUID
import java.io.File
import java.io.IOException
import java.io.InputStream

import akka.actor.Actor
import com.peekaboo.messaging.socket.middleware.BinaryMessageInterceptor
import com.peekaboo.transformation.TextToAudioWatson
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
    case (msg: Send, id: String) =>
      //nice place to add conversion
      //have to check user's state
      //and than if needed perform some actions
      logger.error("Got to message Actor")
      try{
        val rootPath = System.getProperty("catalina.home")
        val rootDir = new File(rootPath + File.separator + "tmp")
        if (!rootDir.exists) rootDir.mkdirs()
        rootDir.mkdirs()
        val uploadedFile = new File(rootDir.getAbsolutePath + File.separator + "aaa.wav")
        if (!uploadedFile.exists) uploadedFile.createNewFile
        val stream=new FileOutputStream(uploadedFile)
        val o=new FileOutputStream(uploadedFile)
        val buffer: Array[Byte] = new Array[Byte](1024)
        var length: Int = 0
        val t=new TextToAudioWatson
        val in:InputStream=t.RunServiceWithDefaults("hello world")
        IOUtils.copy(in, o)
        o.close()
        //      o.write(
        //        Stream.continually(in.read).takeWhile(-1 !=).map(_.toByte).toArray
        //      )
        //        o.close()
      }
      //      var i=in.read(buffer)
      //
      //        do{
      //          stream.write(buffer, 0, length)
      //          i=in.read(buffer)
      //        }
      //        while (i>0)
      //      stream.flush()
      //      stream.close()}
      catch{case e: Exception =>logger.error(e.toString)}



      val action = msg.toMessage(id)
      sendMessage(action)
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