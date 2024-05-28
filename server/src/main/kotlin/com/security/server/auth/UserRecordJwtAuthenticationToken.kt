package com.security.server.auth

import com.security.server.auth.entity.UserRecord
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserRecordJwtAuthenticationToken(
    private val credentials: String,
    private val principal: UserRecord,
): Authentication {
    private var isAuthenticated: Boolean = false

    override fun getName(): String = principal.id.toString()

    override fun getAuthorities(): List<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun getCredentials(): String = credentials

    override fun getDetails(): Any = ""

    override fun getPrincipal(): Any = principal

    override fun isAuthenticated(): Boolean = isAuthenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}
