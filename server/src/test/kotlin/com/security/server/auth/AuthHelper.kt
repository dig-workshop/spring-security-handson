package com.security.server.auth

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*

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

        fun jwtUser(
            sub: String = "sample-subject",
            name: String = "sample-name",
        ): JwtRequestPostProcessor {
            return jwt().jwt {
                it.claim("sub", sub)
                    .claim("name", name)
            }
        }
    }
}