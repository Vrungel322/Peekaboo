package com.peekaboo.security

import com.peekaboo.security.jwt.{JwtAuthenticationProvider, JwtAuthenticationToken}
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication

class JwtAuthenticationInterceptor extends AuthenticationInterceptor{


	private var authenticationProvider: JwtAuthenticationProvider = null
	private val logger = LogManager.getLogger(JwtAuthenticationInterceptor.this)

	@Autowired
	def setAuthenticationProvider(provider: JwtAuthenticationProvider) = this.authenticationProvider = provider

	override def authenticate(headers: HttpHeaders): Authentication = {
		val headersContent = headers.get("Authorization")
		logger.debug("Headers:" + headersContent)
		val header = if (headersContent.size > 0) headersContent.get(0) else ""
		logger.debug(header)

		var authToken: String = ""
		if (!header.startsWith("Bearer ")) {
			logger.debug("No relevant headers were found")
		}
		else {
			authToken = header.substring(7)
		}

		val tokenHeader = new JwtAuthenticationToken(authToken)
		authenticationProvider.authenticate(tokenHeader)
	}
}
