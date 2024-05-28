package com.security.server.auth.service

import com.security.server.auth.UserResponse


class DummyUserService: UserService {
    override fun createOrGet(sub: String, name: String): UserResponse {
        return UserResponse(name =  "", accessToken = "")
    }
}

class SpyUserService: UserService {
    var createOrGet_argument_sub: String? = null
    var createOrGet_argument_name: String? = null
    override fun createOrGet(sub: String, name: String): UserResponse {
        createOrGet_argument_sub = sub
        createOrGet_argument_name = name
        return UserResponse(name =  "", accessToken = "")
    }
}

class StubUserService: UserService {
    var createOrGet_returnValue: UserResponse = UserResponse(name = "", accessToken = "")
    override fun createOrGet(sub: String, name: String): UserResponse {
        return createOrGet_returnValue
    }
}