package com.security.server.auth

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.security.web.SecurityFilterChain
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

        val oauth2LoginFilter = filters.any { it is OAuth2LoginAuthenticationFilter }
        assertTrue(oauth2LoginFilter, "OAuth2LoginAuthenticationFilter should be present in the filter chain")
    }
}