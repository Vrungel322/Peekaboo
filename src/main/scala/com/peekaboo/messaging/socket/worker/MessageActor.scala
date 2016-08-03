package com.peekaboo.messaging.socket.worker

import java.io._
import java.util.UUID

import akka.actor.Actor
import com.peekaboo.messaging.socket.middleware.BinaryMessageInterceptor
import org.apache.logging.log4j.LogManager
import org.springframework.web.socket.{BinaryMessage, WebSocketSession}

import scala.io.Source

class MessageActor(private val socket: WebSocketSession) extends Actor {

  private var activeFile: FileOutputStream = _
  private var fileName: String = _

//  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {

    //here might be a possibility that connection with socket wasn't closed. Have to do it manually
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
//      receiveTextMessage(msg)

      if (msg.getType == Type.Audio) {
        if (activeFile == null) {
          fileName = id + "/" + generateFileName
          activeFile = new FileOutputStream(new File(fileName))
        }
        activeFile.write(msg.getBody)

        if (msg.getParameter(ParameterName.Reason).get == "end") {
          activeFile.close()
          activeFile = null
          splitFile(fileName, id).foreach(sendMessage)
        }

      } else {
        val action = msg.toMessage(id) //Message(msg.getBody, Map(ParameterName.From -> id))
        sendMessage(action)
      }
    case a =>
      logger.debug("Message received but it cannot be resolved")
      logger.debug(a)
  }

  private def splitFile(fileName: String, id: String): Iterator[Send] = {

    val reader = new FileInputStream(new File(fileName))
    val acc = new Array[Byte](reader.available())
    reader.read(acc)

    val header = new Header("SEND", Map(ParameterName.From -> id, ParameterName.Type -> Type.Audio))
    val size = header.size
    val limit = socket.getBinaryMessageSizeLimit
    acc.grouped(limit - size).map(payload => header.toSend(payload))
  }

//  private def receiveTextMessage(message: Send): Unit = {
//    sendTextMessage(message)
//  }

  private def generateFileName: String = UUID.randomUUID.toString

  private def sendMessage(message: Action): Unit = {
    val msg = BinaryMessageInterceptor.compose(message)
    logger.debug("Message:\n" + new String(msg))
    socket.sendMessage(new BinaryMessage(msg))
  }

//  private def receiveAudioMessage(message: Audio): Unit = {
//  }
//
//  private def sendAudioMessage(message: Audio): Unit = {
//  }

  private val logger = LogManager.getLogger(MessageActor.this)
}
