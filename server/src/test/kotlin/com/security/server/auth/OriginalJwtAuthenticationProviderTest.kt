package com.security.server.auth

import com.security.server.auth.authentication.OriginalJwtAuthentication
import com.security.server.auth.coder.AlwaysErrorOriginalJwtDecoder
import com.security.server.auth.coder.DummyOriginalJwtDecoder
import com.security.server.auth.coder.SpyOriginalJwtDecoder
import com.security.server.auth.coder.StubOriginalJwtDecoder
import com.security.server.auth.entity.UserRecord
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken

class OriginalJwtAuthenticationProviderTest {
    @Nested
    inner class Authenticate {
        @Test
        fun JwtDecoderの返り値がprincipalに入ったUserRecordJwtAuthenticationTokenを返す() {
            val stubDecoder = StubOriginalJwtDecoder()
            val expectedPrincipal = UserRecord(subject = "subject", username = "user name")
            stubDecoder.decode_returnValue = expectedPrincipal
            val authenticationProvider = OriginalJwtAuthenticationProvider(stubDecoder)


            val unauthenticatedToken = OriginalJwtAuthentication.unauthenticated("access token")
            val authenticatedToken = authenticationProvider.authenticate(unauthenticatedToken)


            assertEquals(expectedPrincipal, authenticatedToken.principal)
            assertEquals("", authenticatedToken.credentials)
            assertTrue(authenticatedToken.isAuthenticated)
        }

        @Test
        fun Authenticationからアクセストークンを取り出してJwtDecoderに渡す() {
            val spyDecoder = SpyOriginalJwtDecoder()
            val authenticationProvider = OriginalJwtAuthenticationProvider(spyDecoder)


            val unauthenticatedToken = OriginalJwtAuthentication.unauthenticated("access token")
            authenticationProvider.authenticate(unauthenticatedToken)


            assertEquals("access token", spyDecoder.decode_argument_accessToken)
        }

        @Test
        fun アクセストークンのparseに失敗したらBadCredentialExceptionを投げる() {
            val alwaysErrorUserRecordJwtDecoder = AlwaysErrorOriginalJwtDecoder()
            val authenticationProvider = OriginalJwtAuthenticationProvider(alwaysErrorUserRecordJwtDecoder)


            val unauthenticatedToken = OriginalJwtAuthentication.unauthenticated("")
            val exception = assertThrows<BadCredentialsException> { authenticationProvider.authenticate(unauthenticatedToken) }


            assertEquals("Invalid access token", exception.message)
        }
    }

    @Nested
    inner class Supports {
        @Test
        fun サポートしている型を渡された場合Trueを返す() {
            val authenticationProvider = OriginalJwtAuthenticationProvider(DummyOriginalJwtDecoder())


            val supports = authenticationProvider.supports(OriginalJwtAuthentication::class.java)


            assertTrue(supports)
        }

        @Test
        fun サポートされていない型を渡された場合Falseを返す() {
            val authenticationProvider = OriginalJwtAuthenticationProvider(DummyOriginalJwtDecoder())


            val supports = authenticationProvider.supports(OAuth2LoginAuthenticationToken::class.java)


            assertFalse(supports)
        }
    }
}