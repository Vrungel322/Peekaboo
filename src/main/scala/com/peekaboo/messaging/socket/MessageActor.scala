package com.peekaboo.messaging.socket

import akka.actor.Actor
import org.apache.logging.log4j.LogManager
import org.springframework.web.socket.{BinaryMessage, WebSocketSession}

class MessageActor(private val socket: WebSocketSession) extends Actor {



  def receive = {
      case (msg: Send, id: String) =>
        logger.debug(s"User ${msg.getDestination} got message from ${id}")
        receiveTextMessage(msg)
      case a =>
        logger.debug("Message received but it cannot be resolved")
        logger.debug(a)
    }



  private def receiveTextMessage(message: Send): Unit = {
    sendTextMessage(message)
  }

  private def sendTextMessage(message: Action): Unit = {
    socket.sendMessage(new BinaryMessage(Action.compose(message)))
  }

  private def receiveAudioMessage(message: Audio): Unit = {
  }

  private def sendAudioMessage(message: Audio): Unit = {
  }

  private val logger = LogManager.getLogger(MessageActor.this)
}
