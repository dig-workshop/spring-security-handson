package com.security.server.auth

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/me")
    fun me(authentication: Authentication): UserResponse {
        val principal = authentication.principal as OidcUser
        return userService.createOrGet(
            sub = principal.subject,
            name = principal.getClaimAsString("name"),
        )
    }
}