package com.security.server.auth.coder

import com.security.server.auth.entity.UserRecord
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

interface OriginalJwtDecoder {
    fun decode(accessToken: String): UserRecord
}

@Component
class DefaultOriginalJwtDecoder(
    @Value("\${app.jwt.secret}")
    private val secret: String,
    @Value("\${app.jwt.issuer}")
    private val issuer: String,
): OriginalJwtDecoder {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    override fun decode(accessToken: String): UserRecord {
        val parser = Jwts.parser()
            .verifyWith(key)
            .requireIssuer(issuer)
            .build()
        val result = parser.parseSignedClaims(accessToken)

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