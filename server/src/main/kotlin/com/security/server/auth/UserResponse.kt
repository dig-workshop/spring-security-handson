package com.security.server.auth

import java.util.UUID

data class UserResponse(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val accessToken: String,
)
