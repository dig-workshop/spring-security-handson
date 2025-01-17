package com.security.server.auth.service

import com.security.server.auth.UserResponse
import com.security.server.auth.authentication.AcquireAccessTokenUser


class DummyUserService: UserService {
    override fun createOrGet(acquireAccessTokenUser: AcquireAccessTokenUser): UserResponse {
        return UserResponse(name =  "", accessToken = "")
    }
}

class SpyUserService: UserService {
    var createOrGet_argument_acquireAccessTokenUser: AcquireAccessTokenUser? = null
    override fun createOrGet(acquireAccessTokenUser: AcquireAccessTokenUser): UserResponse {
        createOrGet_argument_acquireAccessTokenUser = acquireAccessTokenUser
        return UserResponse(name =  "", accessToken = "")
    }
}

class StubUserService: UserService {
    var createOrGet_returnValue: UserResponse = UserResponse(name = "", accessToken = "")
    override fun createOrGet(acquireAccessTokenUser: AcquireAccessTokenUser): UserResponse {
        return createOrGet_returnValue
    }
}