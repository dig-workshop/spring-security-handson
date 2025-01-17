package com.security.server.auth.coder

import com.security.server.auth.entity.UserRecord
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

interface OriginalJwtEncoder {
    fun encode(userRecord: UserRecord): String
}

@Component
class DefaultOriginalJwtEncoder(
    @Value("\${app.jwt.secret}")
    private val secret: String,
    @Value("\${app.jwt.expiration-time}")
    private val expirationTime: Int,
    @Value("\${app.jwt.issuer}")
    private val issuer: String,
): OriginalJwtEncoder {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())
    override fun encode(userRecord: UserRecord): String {
        val issuedAt = System.currentTimeMillis()
        val expiresAt = issuedAt + expirationTime * 1000
        val claims = Jwts.claims()
            .id(UUID.randomUUID().toString())
            .subject(userRecord.subject)
            .expiration(Date(expiresAt))
            .issuer(issuer)
            .issuedAt(Date(issuedAt))
            .add("name", userRecord.username)
            .add("userId", userRecord.id)
            .build()
        return Jwts.builder()
            .claims(claims)
            .signWith(key)
            .compact()
    }
}
