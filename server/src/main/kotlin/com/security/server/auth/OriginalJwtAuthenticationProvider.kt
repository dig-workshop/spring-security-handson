package com.security.server.auth

import com.security.server.auth.coder.OriginalJwtDecoder
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

class OriginalJwtAuthenticationProvider(
    private val jwtDecoder: OriginalJwtDecoder
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val accessToken = (authentication as OriginalJwtAuthentication).credentials
        try {
            val userRecord = jwtDecoder.decode(accessToken)
            val authenticatedToken = OriginalJwtAuthentication("", userRecord)
            authenticatedToken.isAuthenticated = true
            return authenticatedToken
        } catch(exception: Exception) {
            throw BadCredentialsException("Invalid access token", exception)
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return OriginalJwtAuthentication::class.java.isAssignableFrom(authentication)
    }
}