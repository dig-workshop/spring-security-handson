package com.security.server.auth.service

import com.security.server.auth.UserRepository
import com.security.server.auth.UserResponse
import com.security.server.auth.entity.UserRecord
import org.springframework.stereotype.Service

interface UserService {
    fun createOrGet(subject: String, username: String): UserResponse
}

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
): UserService {
    override fun createOrGet(subject: String, username: String): UserResponse {
        val userRecord = userRepository.findBySubject(subject)
            ?: userRepository.save(UserRecord(subject = subject, username = username))
        return UserResponse(userRecord.id, userRecord.username)
    }
}
