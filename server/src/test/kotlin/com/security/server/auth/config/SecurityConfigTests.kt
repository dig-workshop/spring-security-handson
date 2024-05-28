package com.security.server.auth.config

import com.security.server.auth.AuthHelper.Companion.oidcUser
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
        MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()

        val filterChain = context.getBean(SecurityFilterChain::class.java)
        val filters = filterChain.filters

        val filterExists = filters.any { it is OAuth2LoginAuthenticationFilter }
        assertTrue(filterExists, "OAuth2LoginAuthenticationFilter should be present in the filter chain")
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
         MockMvcBuilders
             .webAppContextSetup(context)
             .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
             .build()


        val filterChain = context.getBean(SecurityFilterChain::class.java)
        val filters = filterChain.filters


        val filterExists = filters.any { it is BearerTokenAuthenticationFilter }
        assertTrue(filterExists, "BearerTokenAuthenticationFilter should be present in the filter chain")
    }
}