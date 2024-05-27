package com.security.server.auth

import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/me")
    fun me(authentication: Authentication): UserResponse {
        if (authentication.principal is OidcUser) {
            val oidcUser = authentication.principal as OidcUser
            return userService.createOrGet(
                sub = oidcUser.subject,
                name = oidcUser.getClaimAsString("name"),
            )
        } else if ( authentication.principal is Jwt) {
            val jwt = authentication.principal as Jwt
            return userService.createOrGet(
                sub = jwt.subject,
                name = jwt.getClaimAsString("name"),
            )
        }
        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
    }
}