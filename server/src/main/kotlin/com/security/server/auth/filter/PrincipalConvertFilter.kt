package com.security.server.auth.filter

import com.security.server.auth.authentication.SocialLoginAuthentication
import com.security.server.auth.authentication.SocialLoginUser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter

class PrincipalConvertFilter: OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null) {
            filterChain.doFilter(request, response)
            return
        }
        if (authentication is JwtAuthenticationToken) {
            val jwt = authentication.principal as Jwt
            val socialUser = SocialLoginUser(jwt.subject, jwt.getClaimAsString("name"))
            setSocialLoginAuthenticationToContext(socialUser)
        }
        if (authentication is OAuth2AuthenticationToken) {
            val oidcUser = authentication.principal as OidcUser
            val socialUser = SocialLoginUser(oidcUser.subject, oidcUser.getClaimAsString("name"))
            setSocialLoginAuthenticationToContext(socialUser)
        }
        filterChain.doFilter(request, response)
    }

    private fun setSocialLoginAuthenticationToContext(socialUser: SocialLoginUser) {
        val socialLoginAuthentication = SocialLoginAuthentication("", socialUser)
        val context = SecurityContextHolder.getContext()
        context.authentication = socialLoginAuthentication
    }
}