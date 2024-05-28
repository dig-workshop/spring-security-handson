package com.security.server.auth

import org.springframework.stereotype.Service

interface UserService {
    fun createOrGet(sub: String, name: String): UserResponse
}

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
    private val jwtEncoder: UserRecordJwtEncoder
): UserService {
    override fun createOrGet(sub: String, name: String): UserResponse {
        val userRecord = userRepository.findBySubject(sub)
            ?: userRepository.save(UserRecord(subject = sub, username = name))
        val accessToken = jwtEncoder.encode(userRecord)
        return UserResponse(userRecord.id, userRecord.username, accessToken)
    }
}
