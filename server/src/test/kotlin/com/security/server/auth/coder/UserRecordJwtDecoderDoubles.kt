package com.security.server.auth.coder

import com.security.server.auth.entity.UserRecord

class DummyUserRecordJwtDecoder: UserRecordJwtDecoder {
    override fun decode(accessToken: String): UserRecord {
        return UserRecord(subject = "", username = "")
    }
}

class SpyUserRecordJwtDecoder: UserRecordJwtDecoder {
    var decode_argument_accessToken: String? = null
    override fun decode(accessToken: String): UserRecord {
        decode_argument_accessToken = accessToken
        return UserRecord(subject = "", username = "")
    }
}

class StubUserRecordJwtDecoder: UserRecordJwtDecoder {
    var decode_returnValue: UserRecord = UserRecord(subject = "", username = "")
    override fun decode(accessToken: String): UserRecord {
        return decode_returnValue
    }
}

class AlwaysErrorUserRecordJwtDecoder: UserRecordJwtDecoder {
    override fun decode(accessToken: String): UserRecord {
        throw java.security.SignatureException()
    }
}