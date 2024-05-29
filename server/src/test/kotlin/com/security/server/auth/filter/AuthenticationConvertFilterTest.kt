package com.security.server.auth.filter

import com.security.server.auth.authentication.AcquireAccessTokenAuthentication
import com.security.server.auth.authentication.AcquireAccessTokenUser
import com.security.server.auth.authentication.OriginalJwtAuthentication
import com.security.server.auth.entity.UserRecord
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.time.Instant

class AuthenticationConvertFilterTest {
    @Test
    fun authenticationがnullならそのままdoFilterを実行する() {
        val filter = AuthenticationConvertFilter()
        val mockRequest = MockHttpServletRequest()
        val mockResponse = MockHttpServletResponse()
        val spyFilterChain = SpyFilterChain()
        SecurityContextHolder.clearContext()


        filter.doFilter(mockRequest, mockResponse, spyFilterChain)


        assertEquals(mockRequest, spyFilterChain.doFilter_argument_request)
        assertEquals(mockResponse, spyFilterChain.doFilter_argument_response)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun JwtAuthenticationTokenをAcquireAccessTokenAuthenticationに変換してContextHolderにセットする() {
        val filter = AuthenticationConvertFilter()
        val mockRequest = MockHttpServletRequest()
        val mockResponse = MockHttpServletResponse()
        val spyFilterChain = SpyFilterChain()
        val context = SecurityContextHolder.getContext()
        val jwt = Jwt(
            "id_token",
            Instant.now(),
            Instant.now().plusSeconds(1),
            mapOf(
                "key" to "value",
            ),
            mapOf(
                "sub" to "subject",
                "name" to "user name",
            ),
        )
        context.authentication = JwtAuthenticationToken(jwt)


        filter.doFilter(mockRequest, mockResponse, spyFilterChain)


        val authentication = SecurityContextHolder.getContext().authentication
        assertTrue(authentication is AcquireAccessTokenAuthentication)
        assertEquals("", authentication.credentials)
        val principal = authentication.principal as AcquireAccessTokenUser
        assertEquals("subject", principal.subject)
        assertEquals("user name", principal.name)
        assertEquals(mockRequest, spyFilterChain.doFilter_argument_request)
        assertEquals(mockResponse, spyFilterChain.doFilter_argument_response)
    }

    @Test
    fun OAuth2AuthenticationTokenをAcquireAccessTokenAuthenticationに変換してContextHolderにセットする() {
        val filter = AuthenticationConvertFilter()
        val mockRequest = MockHttpServletRequest()
        val mockResponse = MockHttpServletResponse()
        val spyFilterChain = SpyFilterChain()
        val context = SecurityContextHolder.getContext()
        val idToken = OidcIdToken(
                "id_token",
                Instant.now(),
                Instant.now().plusSeconds(1),
                mapOf(
                    "sub" to "subject",
                    "name" to "user name",
                ),
            )
        val oidcUser = DefaultOidcUser(emptyList(), idToken)
        context.authentication = OAuth2AuthenticationToken(oidcUser, mutableListOf(), "google")


        filter.doFilter(mockRequest, mockResponse, spyFilterChain)


        val authentication = SecurityContextHolder.getContext().authentication
        assertTrue(authentication is AcquireAccessTokenAuthentication)
        assertEquals("", authentication.credentials)
        val principal = authentication.principal as AcquireAccessTokenUser
        assertEquals("subject", principal.subject)
        assertEquals("user name", principal.name)
        assertEquals(mockRequest, spyFilterChain.doFilter_argument_request)
        assertEquals(mockResponse, spyFilterChain.doFilter_argument_response)
    }

    @Test
    fun OriginalJwtAuthenticationをAcquireAccessTokenAuthenticationに変換してContextHolderにセットする() {
        val filter = AuthenticationConvertFilter()
        val mockRequest = MockHttpServletRequest()
        val mockResponse = MockHttpServletResponse()
        val spyFilterChain = SpyFilterChain()
        val context = SecurityContextHolder.getContext()
        context.authentication = OriginalJwtAuthentication(
            "",
            UserRecord(subject = "subject", username = "user name")
        )


        filter.doFilter(mockRequest, mockResponse, spyFilterChain)


        val authentication = SecurityContextHolder.getContext().authentication
        assertTrue(authentication is AcquireAccessTokenAuthentication)
        assertEquals("", authentication.credentials)
        val principal = authentication.principal as AcquireAccessTokenUser
        assertEquals("subject", principal.subject)
        assertEquals("user name", principal.name)
        assertEquals(mockRequest, spyFilterChain.doFilter_argument_request)
        assertEquals(mockResponse, spyFilterChain.doFilter_argument_response)
    }
}