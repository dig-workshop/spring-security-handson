package com.security.server.auth.config

import com.security.server.auth.filter.OriginalJwtAuthenticationFilter
import com.security.server.auth.OriginalJwtAuthenticationProvider
import com.security.server.auth.coder.OriginalJwtDecoder
import com.security.server.auth.filter.PrincipalConvertFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun authSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.securityMatcher("/oauth2/authorization/**", "/login/oauth2/code/**", "/auth/api/users/me", "/logout")
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/api/users/me").authenticated()
                it.requestMatchers( "/logout").permitAll()
            }
            .oauth2Login {
                it.defaultSuccessUrl("http://localhost:5173", true)
            }
            .oauth2ResourceServer { it.jwt {} }
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
            }
            .logout {
                it.logoutSuccessUrl("http://localhost:5173")
            }
            .addFilterBefore(PrincipalConvertFilter(), AnonymousAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun appSecurityFilterChain(
        http: HttpSecurity,
        authenticationManager: AuthenticationManager,
    ): SecurityFilterChain {
        http.securityMatcher("/api/**")
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/api/diary").authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
            }
            .addFilterBefore(OriginalJwtAuthenticationFilter(authenticationManager), AnonymousAuthenticationFilter::class.java)
        return http.build()
    }
}

@Configuration
class OriginalJwtAuthenticationConfig {
    @Bean
    fun originalJwtAuthenticationProvider(originalJwtDecoder: OriginalJwtDecoder): OriginalJwtAuthenticationProvider {
        return OriginalJwtAuthenticationProvider(originalJwtDecoder)
    }

    @Bean
    fun authenticationManager(
        authenticationProviders: List<AuthenticationProvider>
    ): AuthenticationManager {
        return ProviderManager(authenticationProviders)
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

