package com.security.server.auth

import org.springframework.stereotype.Service

interface UserService {
    fun createOrGet(oid: String, name: String): UserResponse
}

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
): UserService {
    override fun createOrGet(oid: String, name: String): UserResponse {
        val userRecord = userRepository.findByOid(oid)
            ?: userRepository.save(UserRecord(oid = oid, username = name))
        return UserResponse(userRecord.id, userRecord.username)
    }
}
