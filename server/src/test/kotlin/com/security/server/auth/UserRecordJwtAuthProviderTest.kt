package com.security.server.auth

import com.security.server.auth.coder.AlwaysErrorUserRecordJwtDecoder
import com.security.server.auth.coder.DummyUserRecordJwtDecoder
import com.security.server.auth.coder.SpyUserRecordJwtDecoder
import com.security.server.auth.coder.StubUserRecordJwtDecoder
import com.security.server.auth.entity.UserRecord
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken

class UserRecordJwtAuthProviderTest {
    @Nested
    inner class Authenticate {
        @Test
        fun JwtDecoderの返り値がprincipalに入ったUserRecordJwtAuthenticationTokenを返す() {
            val stubDecoder = StubUserRecordJwtDecoder()
            val expectedPrincipal = UserRecord(subject = "subject", username = "user name")
            stubDecoder.decode_returnValue = expectedPrincipal
            val authenticationProvider = UserRecordJwtAuthProvider(stubDecoder)


            val unauthenticatedToken = UserRecordJwtAuthenticationToken("access token", UserRecord(subject = "", username = ""))
            val authenticatedToken = authenticationProvider.authenticate(unauthenticatedToken)


            assertEquals(expectedPrincipal, authenticatedToken.principal)
            assertEquals("", authenticatedToken.credentials)
            assertTrue(authenticatedToken.isAuthenticated)
        }

        @Test
        fun Authenticationからアクセストークンを取り出してJwtDecoderに渡す() {
            val spyDecoder = SpyUserRecordJwtDecoder()
            val authenticationProvider = UserRecordJwtAuthProvider(spyDecoder)


            val unauthenticatedToken = UserRecordJwtAuthenticationToken("access token", UserRecord(subject = "", username = ""))
            authenticationProvider.authenticate(unauthenticatedToken)


            assertEquals("access token", spyDecoder.decode_argument_accessToken)
        }

        @Test
        fun アクセストークンのparseに失敗したらBadCredentialExceptionを投げる() {
            val alwaysErrorUserRecordJwtDecoder = AlwaysErrorUserRecordJwtDecoder()
            val authenticationProvider = UserRecordJwtAuthProvider(alwaysErrorUserRecordJwtDecoder)


            val unauthenticatedToken = UserRecordJwtAuthenticationToken("", UserRecord(subject = "", username = ""))
            val exception = assertThrows<BadCredentialsException> { authenticationProvider.authenticate(unauthenticatedToken) }


            assertEquals("Invalid access token", exception.message)
        }
    }

    @Nested
    inner class Supports {
        @Test
        fun サポートしている型を渡された場合Trueを返す() {
            val authenticationProvider = UserRecordJwtAuthProvider(DummyUserRecordJwtDecoder())


            val supports = authenticationProvider.supports(UserRecordJwtAuthenticationToken::class.java)


            assertTrue(supports)
        }

        @Test
        fun サポートされていない型を渡された場合Falseを返す() {
            val authenticationProvider = UserRecordJwtAuthProvider(DummyUserRecordJwtDecoder())


            val supports = authenticationProvider.supports(OAuth2LoginAuthenticationToken::class.java)


            assertFalse(supports)
        }
    }
}