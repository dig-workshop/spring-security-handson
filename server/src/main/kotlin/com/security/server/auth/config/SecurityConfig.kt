package com.security.server.auth.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
        http.authorizeHttpRequests {
            it.requestMatchers("/auth/api/users/me").authenticated()
        }
        http.oauth2Login {
            it.defaultSuccessUrl("http://localhost:5173", true)
        }
        http.exceptionHandling {
            it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
        }
        http.logout { it.logoutSuccessUrl("http://localhost:5173") }
        return http.build()
    }
}

class CustomAuthenticationEntryPoint: AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
    }
}
