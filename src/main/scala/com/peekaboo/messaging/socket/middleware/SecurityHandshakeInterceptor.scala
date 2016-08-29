package com.peekaboo.messaging.socket.middleware

import java.util

import com.peekaboo.model.entity.User
import com.peekaboo.security.AuthenticationInterceptor
import org.apache.logging.log4j.LogManager
import org.springframework.http.server.{ServerHttpRequest, ServerHttpResponse}
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

import scala.collection.JavaConverters._

//Intercepts initial dialog to authenticate a user
class SecurityHandshakeInterceptor(authenticationInterceptor: AuthenticationInterceptor) extends HandshakeInterceptor {
  override def beforeHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, attributes: util.Map[String, AnyRef]): Boolean = {

    logger.debug("Received new connection request.")
    logger.debug("Attempting to authenticate it")
    val authentication = authenticationInterceptor.authenticate(request.getHeaders)
    logger.error("took here");
    //user has to have a "USER" authority to be able user the chat
    if (!authentication.getAuthorities.asScala.exists(_.getAuthority == "USER")) {
      logger.debug("Error authenticating user")
      false
    }
    else {
      logger.debug("User was successfully authenticated")
      //remember user id for future needs
      request.getHeaders.add("id", authentication.getPrincipal.asInstanceOf[User].getId.toString)
      true
    }

  }

  private val logger = LogManager.getLogger(SecurityHandshakeInterceptor.this)

  override def afterHandshake(serverHttpRequest: ServerHttpRequest, serverHttpResponse: ServerHttpResponse, webSocketHandler: WebSocketHandler, e: Exception): Unit = {}
}
