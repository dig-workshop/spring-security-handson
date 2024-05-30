package com.security.server.auth.service

import com.security.server.auth.UserRepository
import com.security.server.auth.UserResponse
import com.security.server.auth.authentication.AcquireAccessTokenUser
import com.security.server.auth.entity.UserRecord
import org.springframework.stereotype.Service

interface UserService {
    fun createOrGet(acquireAccessTokenUser: AcquireAccessTokenUser): UserResponse
}

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
): UserService {
    override fun createOrGet(acquireAccessTokenUser: AcquireAccessTokenUser): UserResponse {
        val userRecord = userRepository.findBySubject(acquireAccessTokenUser.subject)
            ?: userRepository.save(UserRecord(subject = acquireAccessTokenUser.subject, username = acquireAccessTokenUser.name))
        return UserResponse(userRecord.id, userRecord.username)
    }
}
