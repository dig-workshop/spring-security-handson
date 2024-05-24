package com.security.server.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserRecord, Long> {
    fun findByOid(oid: String): UserRecord?
}
