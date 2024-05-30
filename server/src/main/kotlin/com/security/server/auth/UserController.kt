package com.security.server.auth

import com.security.server.auth.authentication.SocialLoginUser
import com.security.server.auth.service.UserService
import org.springframework.security.core.Authentication
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
        val socialLoginUser = authentication.principal as SocialLoginUser
        return userService.createOrGet(socialLoginUser)
    }
}