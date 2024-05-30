package com.security.server.auth.service

import com.security.server.auth.UserResponse
import com.security.server.auth.authentication.SocialLoginUser


class DummyUserService: UserService {
    override fun createOrGet(socialLoginUser: SocialLoginUser): UserResponse {
        return UserResponse(name =  "")
    }
}

class SpyUserService: UserService {
    var createOrGet_argument_socialLoginUser: SocialLoginUser? = null
    override fun createOrGet(socialLoginUser: SocialLoginUser): UserResponse {
        createOrGet_argument_socialLoginUser = socialLoginUser
        return UserResponse(name =  "")
    }
}

class StubUserService: UserService {
    var createOrGet_returnValue: UserResponse = UserResponse(name = "")
    override fun createOrGet(socialLoginUser: SocialLoginUser): UserResponse {
        return createOrGet_returnValue
    }
}