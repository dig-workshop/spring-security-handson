package com.security.server.auth

import com.security.server.auth.authentication.OriginalJwtAuthentication
import com.security.server.auth.coder.OriginalJwtDecoder
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

class OriginalJwtAuthenticationProvider(
    private val jwtDecoder: OriginalJwtDecoder
): AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        TODO("Not yet implemented")
    }

    override fun supports(authentication: Class<*>): Boolean {
        TODO("Not yet implemented")
    }
}