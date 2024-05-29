package com.security.server.auth

import com.security.server.auth.AuthHelper.Companion.jwtUser
import com.security.server.auth.AuthHelper.Companion.oidcUser
import com.security.server.auth.service.DummyUserService
import com.security.server.auth.service.SpyUserService
import com.security.server.auth.service.StubUserService
import com.security.server.auth.service.UserService
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.security.web.FilterChainProxy
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@SpringBootTest
class UserControllerTests {
	@Autowired
	private lateinit var springSecurityFilterChain: FilterChainProxy

	@Test
	fun 未認証の場合401を返す() {
		val dummyUserService = DummyUserService()
		val mockMvc = buildMvcWith(dummyUserService)
		val result = mockMvc.get("/auth/api/users/me")


		result.andExpect { status { isUnauthorized() } }
	}

	@Nested
	inner class OAuth2Login {
		@Test
		fun 認証済みの場合はサービスに正しい引数を渡す() {
			val spyUserService = SpyUserService()
			val mockMvc = buildMvcWith(spyUserService)
			val oidcUser = oidcUser("subject", "Tanachu")

			mockMvc.get("/auth/api/users/me") {
				with(oidcUser)
			}


			assertEquals("subject", spyUserService.createOrGet_argument_acquireAccessTokenUser?.subject)
			assertEquals("Tanachu", spyUserService.createOrGet_argument_acquireAccessTokenUser?.name)
		}
	}

	@Nested
	inner class BearerToken {
		@Test
		fun 認証済みの場合はサービスに正しい引数を渡す() {
			val spyUserService = SpyUserService()
			val mockMvc = buildMvcWith(spyUserService)
			val jwt = jwtUser("subject", "Tanachu")

			mockMvc.get("/auth/api/users/me") {
				with(jwt)
			}


			assertEquals("subject", spyUserService.createOrGet_argument_acquireAccessTokenUser?.subject)
			assertEquals("Tanachu", spyUserService.createOrGet_argument_acquireAccessTokenUser?.name)
		}
	}

	@Test
	fun サービスの返り値を返す() {
		val stubUserService = StubUserService()
		stubUserService.createOrGet_returnValue = UserResponse(name = "Yusuke", accessToken = "accessToken")
		val mockMvc = buildMvcWith(stubUserService)
		val user = oidcUser()


		val result = mockMvc.get("/auth/api/users/me") {
			with(user)
		}


		result.andExpect {
			status { isOk() }
			jsonPath("$.name", equalTo("Yusuke"))
			jsonPath("$.accessToken", equalTo("accessToken"))
		}
	}

	private fun buildMvcWith(userService: UserService): MockMvc {
		return MockMvcBuilders
			.standaloneSetup(UserController(userService))
			.apply<StandaloneMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
			.build()
	}
}


