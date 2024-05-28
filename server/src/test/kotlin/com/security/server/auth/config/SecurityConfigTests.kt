package com.security.server.auth.config

import com.security.server.auth.AuthHelper.Companion.oidcUser
import com.security.server.auth.OriginalJwtAuthenticationFilter
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.security.web.SecurityFilterChain
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class SecurityConfigTests {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Test
    fun oAuth2Loginが有効になっている() {
        val authFilterChain = context.getBean("authSecurityFilterChain", SecurityFilterChain::class.java)
        val appFilterChain = context.getBean("appSecurityFilterChain", SecurityFilterChain::class.java)


        val filterExistsInAuth = authFilterChain.filters.any { it is OAuth2LoginAuthenticationFilter }
        val filterExistsInApp = appFilterChain.filters.any { it is OAuth2LoginAuthenticationFilter }
        assertTrue(filterExistsInAuth, "OAuth2LoginAuthenticationFilter should be present in the auth filter chain")
        assertFalse(filterExistsInApp, "OAuth2LoginAuthenticationFilter should not be present in the app filter chain")
    }

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
    fun jwt認証が有効になっている() {
        val authFilterChain = context.getBean("authSecurityFilterChain", SecurityFilterChain::class.java)
        val appFilterChain = context.getBean("appSecurityFilterChain", SecurityFilterChain::class.java)


        val filterExistsInAuth = authFilterChain.filters.any { it is BearerTokenAuthenticationFilter }
        val filterExistsInApp = appFilterChain.filters.any { it is BearerTokenAuthenticationFilter }
        assertTrue(filterExistsInAuth, "BearerTokenAuthenticationFilter should be present in the auth filter chain")
        assertFalse(filterExistsInApp, "BearerTokenAuthenticationFilter should not be present in the app filter chain")
    }

    @Test
    fun OriginalJwtAuthenticationFilterが有効になっている() {
        val authFilterChain = context.getBean("authSecurityFilterChain", SecurityFilterChain::class.java)
        val appFilterChain = context.getBean("appSecurityFilterChain", SecurityFilterChain::class.java)


        val filterExistsInAuth = authFilterChain.filters.any { it is OriginalJwtAuthenticationFilter }
        val filterExistsInApp = appFilterChain.filters.any { it is OriginalJwtAuthenticationFilter }
        assertFalse(filterExistsInAuth, "OriginalJwtAuthenticationFilter should not be present in the auth filter chain")
        assertTrue(filterExistsInApp, "OriginalJwtAuthenticationFilter should be present in the app filter chain")
    }
}