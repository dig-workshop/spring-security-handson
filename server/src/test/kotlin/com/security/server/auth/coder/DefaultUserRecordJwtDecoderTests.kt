package com.security.server.auth.coder

import com.security.server.auth.entity.UserRecord
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.IncorrectClaimException
import io.jsonwebtoken.security.SignatureException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DefaultUserRecordJwtDecoderTests {
    val testSecret = "secret-string-for-jwt-decoding-test"

    @Test
    fun アクセストークンをparseしてUserRecordを返す() {
        val userRecord = UserRecord(subject = "subject", username = "yusuke")
        val encoder = DefaultUserRecordJwtEncoder(testSecret, 3600, "https://hoge.com")
        val accessToken = encoder.encode(userRecord)


        val sut = DefaultUserRecordJwtDecoder(testSecret, "https://hoge.com")
        val result = sut.decode(accessToken)


        assertEquals(userRecord, result)
    }

    @Test
    fun 有効期限が切れている場合エラーを投げる() {
        val userRecord = UserRecord(subject = "subject", username = "yusuke")
        val encoder = DefaultUserRecordJwtEncoder(testSecret, 1, "https://hoge.com")
        val accessToken = encoder.encode(userRecord)


        Thread.sleep(1200)
        val sut = DefaultUserRecordJwtDecoder(testSecret, "https://hoge.com")


        assertThrows<ExpiredJwtException> { sut.decode(accessToken) }
    }

    @Test
    fun Issuerが違う場合エラーを投げる() {
        val userRecord = UserRecord(subject = "subject", username = "yusuke")
        val encoder = DefaultUserRecordJwtEncoder(testSecret, 3600, "https://hoge.com")
        val accessToken = encoder.encode(userRecord)


        val sut = DefaultUserRecordJwtDecoder(testSecret, "https://example.com")


        assertThrows<IncorrectClaimException> { sut.decode(accessToken) }
    }

    @Test
    fun シークレットが違う場合エラーを投げる() {
        val userRecord = UserRecord(subject = "subject", username = "yusuke")
        val encoder = DefaultUserRecordJwtEncoder(testSecret + "1", 3600, "https://hoge.com")
        val accessToken = encoder.encode(userRecord)


        val sut = DefaultUserRecordJwtDecoder(testSecret, "https://hoge.com")


        assertThrows<SignatureException> { sut.decode(accessToken) }
    }

    @Test
    fun アクセストークンが改ざんされている場合エラーを投げる() {
        val userRecord = UserRecord(subject = "subject", username = "yusuke")
        val encoder = DefaultUserRecordJwtEncoder(testSecret, 3600, "https://hoge.com")
        val accessToken = encoder.encode(userRecord)


        val sut = DefaultUserRecordJwtDecoder(testSecret, "https://hoge.com")


        assertThrows<SignatureException> { sut.decode(accessToken + "a") }
    }
}