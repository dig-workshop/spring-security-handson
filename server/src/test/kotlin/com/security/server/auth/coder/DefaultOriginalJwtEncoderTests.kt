package com.security.server.auth.coder

import com.security.server.auth.entity.UserRecord
import org.junit.jupiter.api.Test
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class DefaultOriginalJwtEncoderTests {
    private val testSecret = "secret-string-for-jwt-encoding-test"

    @Test
    fun 正しいクレームを含めて指定したシークレットを使って署名する() {
        val sut = DefaultOriginalJwtEncoder(testSecret, 3600, "https://hoge.com")
        val userRecord = UserRecord(subject = "subject", username = "yusuke")
        val resultAccessToken = sut.encode(userRecord)


        val result: Jws<Claims> =
            Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(testSecret.toByteArray()))
                .build()
                .parseSignedClaims(resultAccessToken)


        assertEquals("subject", result.payload.subject)
        assertEquals("yusuke", result.payload["name"])
        assertEquals(userRecord.id.toString(), result.payload["userId"])
        assertTrue(System.currentTimeMillis() < result.payload.expiration.time)
        assertEquals(3600000, result.payload.expiration.time - result.payload.issuedAt.time)
        assertEquals("https://hoge.com", result.payload.issuer)
    }
}