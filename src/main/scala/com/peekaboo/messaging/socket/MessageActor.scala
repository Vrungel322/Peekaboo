package com.peekaboo.messaging.socket


import org.springframework.web.socket.WebSocketSession

import scala.actors.Actor


class MessageActor(sock: WebSocketSession) extends Actor{
	private val socket = sock


	override def act(): Unit = {
		loop {
			receive {
				case msg @ TextMessage(_, _, _) => receiveTextMessage(msg)
				case msg @AudioMessage(_, _, _) => receiveAudioMessage(msg)
			}
		}
 	}

	private def receiveTextMessage(message: TextMessage): Unit = {

	}

	private def sendTextMessage(message: TextMessage): Unit = {

	}

	private def receiveAudioMessage(message: AudioMessage): Unit = {

	}

	private def sendAudioMessage(message: AudioMessage): Unit = {

	}



}
