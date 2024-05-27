package com.security.server.auth


class DummyUserService: UserService {
    override fun createOrGet(sub: String, name: String): UserResponse {
        return UserResponse(name =  "")
    }
}

class SpyUserService: UserService {
    var createOrGet_argument_sub: String? = null
    var createOrGet_argument_name: String? = null
    override fun createOrGet(sub: String, name: String): UserResponse {
        createOrGet_argument_sub = sub
        createOrGet_argument_name = name
        return UserResponse(name =  "")
    }
}

class StubUserService: UserService {
    var createOrGet_returnValue: UserResponse = UserResponse(name = "")
    override fun createOrGet(sub: String, name: String): UserResponse {
        return createOrGet_returnValue
    }
}