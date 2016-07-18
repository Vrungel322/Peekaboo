package com.peekaboo.messaging.socket

import org.springframework.web.socket.{TextMessage, WebSocketSession}
import org.springframework.web.socket.WebSocketSession
import scala.collection.JavaConverters._
import scala.actors.Actor

class MessageActor(sock: WebSocketSession) extends Actor {
  private val socket = sock

  override def act(): Unit = {
    loop {
      receive {
        case msg@Text(_, _, _) => receiveTextMessage(msg)
        case msg@Audio(_, _, _) => receiveAudioMessage(msg)
      }
    }
  }

  private def receiveTextMessage(message: Text): Unit = {
    sendTextMessage(message)
  }

  private def sendTextMessage(message: Text): Unit = {
    sock.sendMessage(
      new TextMessage(
        MessageHandler.om.writeValueAsString(
          Map(
            "sender" -> message.sender,
            "payload" -> message.text
          ).asJava
        )
      )
    )
  }

  private def receiveAudioMessage(message: Audio): Unit = {
  }

  private def sendAudioMessage(message: Audio): Unit = {
  }
}
