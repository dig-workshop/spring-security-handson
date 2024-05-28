package com.security.server.auth.service

import com.security.server.auth.entity.UserRecord
import com.security.server.auth.UserRepository
import com.security.server.auth.UserResponse
import com.security.server.auth.coder.UserRecordJwtEncoder
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
