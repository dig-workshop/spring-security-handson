package com.security.server.auth.service

import com.security.server.auth.UserResponse


class DummyUserService: UserService {
    override fun createOrGet(subject: String, username: String): UserResponse {
        return UserResponse(name =  "")
    }
}

class SpyUserService: UserService {
    var createOrGet_argument_subject: String? = null
    var createOrGet_argument_username: String? = null
    override fun createOrGet(subject: String, username: String): UserResponse {
        createOrGet_argument_subject = subject
        createOrGet_argument_username = username
        return UserResponse(name =  "")
    }
}

class StubUserService: UserService {
    var createOrGet_returnValue: UserResponse = UserResponse(name = "")
    override fun createOrGet(subject: String, username: String): UserResponse {
        return createOrGet_returnValue
    }
}