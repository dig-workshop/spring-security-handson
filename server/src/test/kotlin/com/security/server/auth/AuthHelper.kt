package com.security.server.auth

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.OidcLoginRequestPostProcessor
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin

class AuthHelper {
    companion object {
        fun oidcUser(
            sub: String = "sample-subject",
            name: String = "sample-name",
        ): OidcLoginRequestPostProcessor {
            return oidcLogin()
                .idToken {
                    it.claim("sub", sub)
                        .claim("name", name)
                }
        }
    }
}