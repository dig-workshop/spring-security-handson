package com.security.server.auth

import com.security.server.auth.entity.UserRecord
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

class DummyAuthenticationManger: AuthenticationManager {
    override fun authenticate(authentication: Authentication): Authentication {
        return authentication
    }
}


class SpyAuthenticationManager: AuthenticationManager {
    var authenticate_argument_authentication: Authentication? = null
    override fun authenticate(authentication: Authentication): Authentication {
        authenticate_argument_authentication = authentication
        return authentication
    }
}

class StubAuthenticationManager: AuthenticationManager {
    var authenticate_returnValue: Authentication = OriginalJwtAuthentication.authenticated(UserRecord(subject = "", username = ""))
    override fun authenticate(authentication: Authentication?): Authentication {
        return authenticate_returnValue
    }
}

class AlwaysErrorAuthenticationManager: AuthenticationManager {
    override fun authenticate(authentication: Authentication?): Authentication {
        throw BadCredentialsException("Invalid access token")
    }
}