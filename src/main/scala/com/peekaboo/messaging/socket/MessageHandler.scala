package com.peekaboo.messaging.socket

import com.fasterxml.jackson.databind.ObjectMapper
import com.peekaboo.model.entity.User
import com.peekaboo.security.AuthenticationInterceptor
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.web.socket.{TextMessage, _}

import scala.collection.JavaConverters._

class MessageHandler extends WebSocketHandler {

  private val logger = LogManager.getLogger(MessageHandler.this)

  private var authenticationInterceptor: AuthenticationInterceptor = _

  private var actorPool: ActorPool = _

  @Autowired
  def setAuthenticationInterceptor(interceptor: AuthenticationInterceptor) = this.authenticationInterceptor = interceptor

  @Autowired
  def setActorPool(pool: ActorPool) = this.actorPool = pool

  override def afterConnectionClosed(webSocketSession: WebSocketSession, closeStatus: CloseStatus): Unit = {

  }

  override def afterConnectionEstablished(session: WebSocketSession): Unit = {

    logger.debug("Received new connection request.")
    logger.debug("Attempting to authenticate it")

    val user = authenticate(session.getHandshakeHeaders)
    if (user.isEmpty) {
      logger.debug("User don't have permissions to connect to websocket")
      session.close(new CloseStatus(CloseStatus.TLS_HANDSHAKE_FAILURE.getCode, "Authentication error. You are not permitted to connect"))
    }

    val actor = new MessageActor(session)
    actorPool.addActor(user.get.getId, actor)

  }


  override def handleTransportError(session: WebSocketSession, throwable: Throwable): Unit = {
    val response =
      MessageHandler.om.
        writeValueAsString(
          Map(
            "type" -> "transport error",
            "message" -> "what the hack is this error?"
          ).asJava
        )
    session.sendMessage(new TextMessage(response))
  }

  private def authenticate(headers: HttpHeaders): Option[User] = {
    val authentication = authenticationInterceptor.authenticate(headers)
    if (!authentication.getAuthorities.asScala.exists(_.getAuthority == "USER"))
      None
    else
      Some(authentication.getPrincipal.asInstanceOf[User])
  }

  override def handleMessage(session: WebSocketSession, message: WebSocketMessage[_]): Unit = {
    val headers = session.getHandshakeHeaders
    val optionalUser = authenticate(headers)
    if (optionalUser.isEmpty) {
      logger.debug("User are not authenticated")
      val response =
        MessageHandler.om.
          writeValueAsString(
            Map(
              "type" -> "send error",
              "message" -> "Cannot send due to absence of authorization header"
            ).asJava
          )
      session.sendMessage(new TextMessage(response))
    } else {
      val receiver = headers.getFirst("Receiver")
      val senderUser = optionalUser.get
      val msg = message match {
        case message: TextMessage => Text(message.getPayload, receiver, senderUser.getId)

      }
      val actor = actorPool.findActor(senderUser.getId)
      actor ! msg
    }
  }

  override def supportsPartialMessages(): Boolean = true

}

object MessageHandler {
  val om = new ObjectMapper()
}