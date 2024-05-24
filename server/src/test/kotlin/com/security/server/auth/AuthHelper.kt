package com.security.server.auth

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.OidcLoginRequestPostProcessor
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin

class AuthHelper {
    companion object {
        fun oidcUser(
            name: String = "subject",
            oid: String = "oid",
        ): OidcLoginRequestPostProcessor {
            return oidcLogin()
                .idToken {
                    it.claim("sub", name)
                        .claim("oid", oid)
                        .claim("name", name)
                }
        }
    }
}