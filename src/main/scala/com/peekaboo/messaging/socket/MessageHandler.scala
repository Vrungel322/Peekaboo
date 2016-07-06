package com.peekaboo.messaging.socket



import com.fasterxml.jackson.databind.ObjectMapper
import com.peekaboo.controller.utils.ErrorType
import com.peekaboo.model.entity.User
import com.peekaboo.security.AuthenticationInterceptor
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.web.socket._

import scala.collection.JavaConverters._

class MessageHandler extends WebSocketHandler{

	private val logger = LogManager.getLogger(MessageHandler.getClass)

	private var authenticationInterceptor: AuthenticationInterceptor = null

	private var actorPool: ActorPool = null

	@Autowired
	def setAuthenticationInterceptor(interceptor: AuthenticationInterceptor) = this.authenticationInterceptor = interceptor

	@Autowired
	def setActorPool(pool: ActorPool) = this.actorPool = pool

	override def afterConnectionClosed(webSocketSession: WebSocketSession, closeStatus: CloseStatus): Unit = {

	}

	override def afterConnectionEstablished(session: WebSocketSession): Unit = {

		logger.debug("Received new connection request.")
		logger.debug("Attempting to authenticate it")
		val authentication = authenticationInterceptor.authenticate(session.getHandshakeHeaders)

		if (!authentication.getAuthorities.asScala.exists(_.getAuthority == "USER")) {
			logger.debug("User don't have permissions to connect to websocket")
//			session.sendMessage(
//				new TextMessage(
//					MessageHandler.stringify(
//						new ErrorResponse(ErrorType.AUTHENTICATION_ERROR, "You don't have permissions to communicate")
//					)
//				)
//			)
			session.close(new CloseStatus(CloseStatus.TLS_HANDSHAKE_FAILURE.getCode, "Authentication error. You are not permitted to connect"))
		}
		val user = authentication.getPrincipal.asInstanceOf[User]

		val actor = new Actor(session)
		actorPool.addActor(user.getId, actor)

	}



	override def handleTransportError(webSocketSession: WebSocketSession, throwable: Throwable): Unit = {

	}

	override def handleMessage(session: WebSocketSession, message: WebSocketMessage[_]): Unit = {
		logger.info(message.getPayload)


		session.sendMessage(new TextMessage("Hello"))
	}

	override def supportsPartialMessages(): Boolean = true

	def getId(httpHeaders: HttpHeaders): String = {
		val httpHeader = httpHeaders.get("Id")
		if (httpHeader.size() > 0) httpHeader.get(0) else null
	}
}

object MessageHandler {
	def stringify(obj: Object): String = {
		new ObjectMapper().writeValueAsString(obj)
	}
}