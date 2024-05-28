package com.security.server.auth

import com.security.server.auth.coder.UserRecordJwtDecoder
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

class UserRecordJwtAuthProvider(
    private val jwtDecoder: UserRecordJwtDecoder
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val accessToken = (authentication as UserRecordJwtAuthenticationToken).credentials
        try {
            val userRecord = jwtDecoder.decode(accessToken)
            val authenticatedToken = UserRecordJwtAuthenticationToken("", userRecord)
            authenticatedToken.isAuthenticated = true
            return authenticatedToken
        } catch(exception: Exception) {
            throw BadCredentialsException("Invalid access token", exception)
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UserRecordJwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}