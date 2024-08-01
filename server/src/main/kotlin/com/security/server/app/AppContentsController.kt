package com.security.server.app

import com.security.server.auth.entity.UserRecord
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class AppContentsController {
    @GetMapping("/diary")
    fun getContents(authentication: Authentication): List<String> {
        val user = authentication.principal as UserRecord
        return listOf(
            "${user.username}は、朝ごはんにパンを食べました",
            "${user.username}は、昼ごはんにラーメンを食べました",
            "${user.username}は、夜ごはんに唐揚げを食べました"
        )
    }
}