package com.peekaboo.messaging.socket

import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.client.WebSocketClient

class Actor(sock: WebSocketSession) {
	private val socket = sock

	def handleMessage() = {

	}

}
