package com.security.server.auth.authentication

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class AcquireAccessTokenAuthentication(
    private val credentials: String,
    private val principal: AcquireAccessTokenUser,
): Authentication {
    private var isAuthenticated: Boolean = true

    override fun getName(): String = principal.subject

    override fun getAuthorities(): List<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun getCredentials(): String = credentials

    override fun getDetails(): Any = ""

    override fun getPrincipal(): Any = principal

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}
