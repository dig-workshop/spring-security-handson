package com.security.server.app

import com.security.server.auth.authentication.OriginalJwtAuthentication
import com.security.server.auth.entity.UserRecord
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@SpringBootTest
class AppContentsControllerTest {
    @Autowired
    private lateinit var springSecurityFilterChain: FilterChainProxy

    @MockBean
    private lateinit var mockAuthenticationManager: AuthenticationManager

    private val validAccessToken = "header.payload.correct-signature"

    private fun withMockUser(subject: String = "subject", username: String = "username") {
        val authenticatedToken = OriginalJwtAuthentication.authenticated(
            UserRecord(
                subject = subject,
                username = username,
            )
        )
        authenticatedToken.isAuthenticated = true
        `when`(mockAuthenticationManager.authenticate(any(OriginalJwtAuthentication::class.java)))
            .thenReturn(authenticatedToken)
    }

    @Test
    fun 未認証の場合401を返す() {
        val mockMvc = buildMvc()


        val result = mockMvc.get("/api/diary")


        result.andExpect { status { isUnauthorized() } }
    }

    @Test
    fun 認証済みの場合は正しい値を返す() {
        val mockMvc = buildMvc()
        withMockUser(username = "又吉")


        val result = mockMvc.get("/api/diary") {
            header("Authorization", "Bearer $validAccessToken")
        }


        result.andExpect {
            status { isOk() }
            jsonPath("$[0]", equalTo("又吉は、朝ごはんにパンを食べました"))
            jsonPath("$[1]", equalTo("又吉は、昼ごはんにラーメンを食べました"))
            jsonPath("$[2]", equalTo("又吉は、夜ごはんに唐揚げを食べました"))
        }
    }

    private fun buildMvc(): MockMvc {
        return MockMvcBuilders
            .standaloneSetup(AppContentsController())
            .apply<StandaloneMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
            .build()
    }
}