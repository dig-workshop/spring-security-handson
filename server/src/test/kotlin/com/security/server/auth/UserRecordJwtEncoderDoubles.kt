package com.security.server.auth

class DummyUserRecordJwtEncoder: UserRecordJwtEncoder {
    override fun encode(userRecord: UserRecord): String {
        return ""
    }
}

class SpyUserRecordJwtEncoder: UserRecordJwtEncoder {
    var encode_argument_userRecord: UserRecord? = null
    override fun encode(userRecord: UserRecord): String {
        encode_argument_userRecord = userRecord
        return ""
    }
}

class StubUserRecordJwtEncoder: UserRecordJwtEncoder {
    var encode_returnValue: String = ""
    override fun encode(userRecord: UserRecord): String {
        return encode_returnValue
    }
}