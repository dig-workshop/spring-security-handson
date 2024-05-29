package com.security.server.auth.config

import com.security.server.auth.AuthHelper.Companion.oidcUser
import com.security.server.auth.OriginalJwtAuthenticationFilter
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@SpringBootTest
class SecurityConfigTests {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Test
    fun ログアウト成功後のリダイレクト先が正しく設定されている() {
        val mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()


        mockMvc.post("/logout") {
            with(oidcUser())
        }
        .andExpect {
            status { is3xxRedirection() }
            redirectedUrl("http://localhost:5173")
        }
    }

    @Nested
    @DisplayName("認証に関わるエンドポイントに適用するSecurityFilterChain")
    inner class AuthSecurityFilterChain {
        private lateinit var authFilterChain: SecurityFilterChain

        @BeforeEach
        fun setUp() {
            authFilterChain = context.getBean("authSecurityFilterChain", SecurityFilterChain::class.java)
        }

        @Test
        fun 適切なURLパターンにマッチする() {
            val authorizationEndpoint = MockHttpServletRequest("GET", "/oauth2/authorization/google")
            assertTrue(authFilterChain.matches(authorizationEndpoint))

            val authorizationCodeReceiveEndpoint = MockHttpServletRequest("GET", "/login/oauth2/code/google")
            assertTrue(authFilterChain.matches(authorizationCodeReceiveEndpoint))

            val userInfoEndpoint = MockHttpServletRequest("GET", "/auth/api/users/me")
            assertTrue(authFilterChain.matches(userInfoEndpoint))

            val logoutEndpoint = MockHttpServletRequest("GET", "/logout")
            assertTrue(authFilterChain.matches(logoutEndpoint))

            val appResourceEndpoint = MockHttpServletRequest("GET", "/api/hoge")
            assertFalse(authFilterChain.matches(appResourceEndpoint))
        }

        @Test
        fun 適切なFilterがFilterChainに追加されている() {
            val oAuth2LoginFilterExists = authFilterChain.filters.any { it is OAuth2LoginAuthenticationFilter }
            assertTrue(oAuth2LoginFilterExists)

            val bearerTokenFilterExists = authFilterChain.filters.any { it is BearerTokenAuthenticationFilter }
            assertTrue(bearerTokenFilterExists)

            val logoutFilterExists = authFilterChain.filters.any { it is LogoutFilter }
            assertTrue(logoutFilterExists)

            val originalJwtFilterExists = authFilterChain.filters.any { it is OriginalJwtAuthenticationFilter }
            assertFalse(originalJwtFilterExists)
        }

        @Test
        fun CSRFフィルターが無効化されている() {
            val csrfFilterExists = authFilterChain.filters.any { it is CsrfFilter }
            assertFalse(csrfFilterExists)
        }
    }

    @Nested
    @DisplayName("アプリケーションのリソースに関わるエンドポイントに適用するSecurityFilterChain")
    inner class AppSecurityFilterChain {
        private lateinit var appFilterChain: SecurityFilterChain

        @BeforeEach
        fun setUp() {
            appFilterChain = context.getBean("appSecurityFilterChain", SecurityFilterChain::class.java)
        }

        @Test
        fun 適切なURLパターンにマッチする() {
            val appResourceEndpoint = MockHttpServletRequest("GET", "/api/hoge")
            assertTrue(appFilterChain.matches(appResourceEndpoint))

            val authorizationEndpoint = MockHttpServletRequest("GET", "/oauth2/authorization/google")
            assertFalse(appFilterChain.matches(authorizationEndpoint))

            val authorizationCodeReceiveEndpoint = MockHttpServletRequest("GET", "/login/oauth2/code/google")
            assertFalse(appFilterChain.matches(authorizationCodeReceiveEndpoint))

            val userInfoEndpoint = MockHttpServletRequest("GET", "/auth/api/users/me")
            assertFalse(appFilterChain.matches(userInfoEndpoint))

            val logoutEndpoint = MockHttpServletRequest("GET", "/logout")
            assertFalse(appFilterChain.matches(logoutEndpoint))
        }

        @Test
        fun 適切なFilterがFilterChainに追加されている() {
            val oauth2LoginFilterExists = appFilterChain.filters.any { it is OAuth2LoginAuthenticationFilter }
            assertFalse(oauth2LoginFilterExists)

            val bearerTokenFilterExists = appFilterChain.filters.any { it is BearerTokenAuthenticationFilter }
            assertFalse(bearerTokenFilterExists)

            val originalJwtFilterExists = appFilterChain.filters.any { it is OriginalJwtAuthenticationFilter }
            assertTrue(originalJwtFilterExists)
        }

        @Test
        fun CSRFフィルターが無効化されている() {
            val csrfFilterExists = appFilterChain.filters.any { it is CsrfFilter }
            assertFalse(csrfFilterExists)
        }
    }
}