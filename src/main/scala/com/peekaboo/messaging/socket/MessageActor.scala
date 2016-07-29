package com.peekaboo.messaging.socket

import akka.actor.Actor
import org.apache.logging.log4j.LogManager
import org.springframework.web.socket.{BinaryMessage, WebSocketSession}

class MessageActor(private val socket: WebSocketSession) extends Actor {

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

      val action = Send(msg.getBody, Map("from" -> id))
      sendMessage(action)
    case a =>
      logger.debug("Message received but it cannot be resolved")
      logger.debug(a)
  }


//  private def receiveTextMessage(message: Send): Unit = {
//    sendTextMessage(message)
//  }

  private def sendMessage(message: Action): Unit = {
    val msg = Action.compose(message)
    logger.debug("Message: " + new String(msg))
    socket.sendMessage(new BinaryMessage(msg))
  }

  private def receiveAudioMessage(message: Audio): Unit = {
  }

  private def sendAudioMessage(message: Audio): Unit = {
  }

  private val logger = LogManager.getLogger(MessageActor.this)
}
