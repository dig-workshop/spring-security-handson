package com.security.server.auth.filter

import com.security.server.auth.*
import com.security.server.auth.authentication.OriginalJwtAuthentication
import com.security.server.auth.entity.UserRecord
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

class OriginalJwtAuthenticationFilterTest {
    @Test
    fun HTTPリクエストからトークンを取り出してOriginalJwtAuthenticationを生成しAuthenticationManagerに渡す() {
        val spyAuthenticationManager = SpyAuthenticationManager()
        val filter = OriginalJwtAuthenticationFilter(spyAuthenticationManager)
        val spyFilterChain = SpyFilterChain()


        val request = MockHttpServletRequest()
        request.addHeader("Authorization", "Bearer access-token")
        val response = MockHttpServletResponse()
        filter.doFilter(request, response, spyFilterChain)


        val authentication = spyAuthenticationManager.authenticate_argument_authentication as OriginalJwtAuthentication
        assertEquals("access-token", authentication.credentials)
        assertFalse(authentication.isAuthenticated)
        assertEquals(request, spyFilterChain.doFilter_argument_request)
        assertEquals(response, spyFilterChain.doFilter_argument_response)
    }

    @Test
    fun AuthenticationManagerが返す認証済みのAuthenticationをContextHolderにセットする() {
        val stubAuthenticationManager = StubAuthenticationManager()
        val userRecord = UserRecord(subject = "subject", username = "user name")
        val expectAuthentication = OriginalJwtAuthentication.authenticated(userRecord)
        stubAuthenticationManager.authenticate_returnValue = expectAuthentication
        val filter = OriginalJwtAuthenticationFilter(stubAuthenticationManager)


        val dummyRequest = MockHttpServletRequest()
        dummyRequest.addHeader("Authorization", "Bearer token")
        filter.doFilter(dummyRequest, MockHttpServletResponse(), DummyFilterChain())


        val authentication = SecurityContextHolder.getContext().authentication
        assertEquals(expectAuthentication, authentication)
    }

    @Test
    fun AuthenticationManagerがエラーを投げたらContextHolderを空にする() {
        val alwaysErrorAuthenticationManager = AlwaysErrorAuthenticationManager()
        val filter = OriginalJwtAuthenticationFilter(alwaysErrorAuthenticationManager)
        val spyFilterChain = SpyFilterChain()
        val context = SecurityContextHolder.getContext()
        context.authentication = OriginalJwtAuthentication.authenticated(UserRecord(subject="", username = ""))


        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        request.addHeader("Authorization", "Bearer access-token")
        filter.doFilter(request, response, spyFilterChain)


        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)
        assertEquals(request, spyFilterChain.doFilter_argument_request)
        assertEquals(response, spyFilterChain.doFilter_argument_response)
    }

    @Test
    fun 無効なトークンがヘッダーに入っていてもエラーにならない() {
        val filter = OriginalJwtAuthenticationFilter(DummyAuthenticationManger())
        val spyFilterChain = SpyFilterChain()


        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        request.addHeader("Authorization", "Bearer malformed token")
        filter.doFilter(request, response, spyFilterChain)


        val authentication = SecurityContextHolder.getContext().authentication
        assertNull(authentication)
        assertEquals(request, spyFilterChain.doFilter_argument_request)
        assertEquals(response, spyFilterChain.doFilter_argument_response)
    }
}