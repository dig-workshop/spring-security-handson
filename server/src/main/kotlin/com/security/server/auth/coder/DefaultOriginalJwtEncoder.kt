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
    override fun encode(userRecord: UserRecord): String {
        TODO("Not yet implemented")
    }
}
