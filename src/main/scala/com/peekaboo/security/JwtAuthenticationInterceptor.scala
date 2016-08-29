package com.peekaboo.security

import com.peekaboo.security.jwt.{JwtAuthenticationProvider, JwtAuthenticationToken}
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication

class JwtAuthenticationInterceptor extends AuthenticationInterceptor {

  private var authenticationProvider: JwtAuthenticationProvider = _
  private val logger = LogManager.getLogger(JwtAuthenticationInterceptor.this)

  @Autowired
  def setAuthenticationProvider(provider: JwtAuthenticationProvider) = this.authenticationProvider = provider

  override def authenticate(headers: HttpHeaders): Authentication = {
    val header = headers.getFirst("Authorization")

    val authToken: String =
      if (!header.startsWith("Bearer ")) {
        logger.debug("No relevant headers were found")
        ""
      } else {
        header.substring(7)
      }

    authenticationProvider.authenticate(new JwtAuthenticationToken(authToken))
  }
}