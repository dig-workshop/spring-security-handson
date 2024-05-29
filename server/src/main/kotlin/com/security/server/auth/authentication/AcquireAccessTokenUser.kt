package com.security.server.auth.authentication

data class AcquireAccessTokenUser(
    val subject: String,
    val name: String,
)
