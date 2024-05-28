package com.security.server.auth.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity(name = "users")
data class UserRecord(
    @Id
    val id: UUID = UUID.randomUUID(),
    val subject: String,
    val username: String,
)
