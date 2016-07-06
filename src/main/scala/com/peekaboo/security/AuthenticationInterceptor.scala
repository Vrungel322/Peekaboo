package com.peekaboo.security

import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication

trait AuthenticationInterceptor {
	def authenticate (headers: HttpHeaders): Authentication
}
