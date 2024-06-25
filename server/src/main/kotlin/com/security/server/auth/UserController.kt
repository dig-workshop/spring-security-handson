package com.security.server.auth

import com.security.server.auth.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/me")
    fun me(authentication: Authentication): UserResponse {
        if (authentication.principal is OidcUser) {
            val oidcUser = authentication.principal as OidcUser
            return userService.createOrGet(oidcUser.subject, oidcUser.getClaimAsString("name"))
        }
        val jwtUser = authentication.principal as Jwt
        return userService.createOrGet(jwtUser.subject, jwtUser.getClaimAsString("name"))
    }
}