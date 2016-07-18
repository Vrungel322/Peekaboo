package com.peekaboo.messaging.socket

import org.springframework.web.socket.WebSocketSession

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

	}

	private def sendTextMessage(message: Text): Unit = {

	}

	private def receiveAudioMessage(message: Audio): Unit = {

	}

	private def sendAudioMessage(message: Audio): Unit = {

	}


}
