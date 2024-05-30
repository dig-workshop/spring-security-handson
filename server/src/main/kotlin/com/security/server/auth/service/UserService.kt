package com.security.server.auth.service

import com.security.server.auth.UserRepository
import com.security.server.auth.UserResponse
import com.security.server.auth.authentication.SocialLoginUser
import com.security.server.auth.entity.UserRecord
import org.springframework.stereotype.Service

interface UserService {
    fun createOrGet(socialLoginUser: SocialLoginUser): UserResponse
}

@Service
class DefaultUserService(
    private val userRepository: UserRepository,
): UserService {
    override fun createOrGet(socialLoginUser: SocialLoginUser): UserResponse {
        val userRecord = userRepository.findBySubject(socialLoginUser.subject)
            ?: userRepository.save(UserRecord(subject = socialLoginUser.subject, username = socialLoginUser.name))
        return UserResponse(userRecord.id, userRecord.username)
    }
}
