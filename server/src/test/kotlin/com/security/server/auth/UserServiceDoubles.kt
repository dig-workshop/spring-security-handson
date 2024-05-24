package com.security.server.auth


class DummyUserService: UserService {
    override fun createOrGet(oid: String, name: String): UserResponse {
        return UserResponse(name =  "")
    }
}

class SpyUserService: UserService {
    var createOrGet_argument_oid: String? = null
    var createOrGet_argument_name: String? = null
    override fun createOrGet(oid: String, name: String): UserResponse {
        createOrGet_argument_oid = oid
        createOrGet_argument_name = name
        return UserResponse(name =  "")
    }
}

class StubUserService: UserService {
    var createOrGet_returnValue: UserResponse = UserResponse(name = "")
    override fun createOrGet(oid: String, name: String): UserResponse {
        return createOrGet_returnValue
    }
}