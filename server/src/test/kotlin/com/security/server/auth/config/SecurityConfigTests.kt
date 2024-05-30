package com.security.server.auth.config

import com.security.server.auth.AuthHelper.Companion.oidcUser
import com.security.server.auth.filter.AuthenticationConvertFilter
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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

    @Test
    fun 適切なFilterがFilterChainに追加されている() {
        val oAuth2LoginFilterExists = securityFilterChain.filters.any { it is OAuth2LoginAuthenticationFilter }
        assertTrue(oAuth2LoginFilterExists)

        val bearerTokenFilterExists = securityFilterChain.filters.any { it is BearerTokenAuthenticationFilter }
        assertTrue(bearerTokenFilterExists)

        val logoutFilterExists = securityFilterChain.filters.any { it is LogoutFilter }
        assertTrue(logoutFilterExists)

        val convertFilterExist = securityFilterChain.filters.any { it is AuthenticationConvertFilter }
        assertTrue(convertFilterExist)
    }

    @Test
    fun CSRFフィルターが無効化されている() {
        val csrfFilterExists = securityFilterChain.filters.any { it is CsrfFilter }
        assertFalse(csrfFilterExists)
    }
}