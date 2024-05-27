package com.security.server.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class OriginalJwtDecoder(
    @Value("\${app.jwt.secret}")
    private val secret: String,
    @Value("\${app.jwt.issuer}")
    private val issuer: String,
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun decode(accessToken: String): UserRecord {
        val result = Jwts.parser()
            .verifyWith(key)
            .requireIssuer(issuer)
            .build()
            .parseSignedClaims(accessToken)

        val userId = result.payload["userId"] as String
        val userName = result.payload["name"] as String
        val subject = result.payload.subject as String
        return UserRecord(
            id = UUID.fromString(userId),
            username = userName,
            subject = subject
        )
    }
}