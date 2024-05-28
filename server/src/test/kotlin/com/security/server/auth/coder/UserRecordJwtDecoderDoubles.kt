package com.security.server.auth.coder

import com.security.server.auth.entity.UserRecord

class DummyOriginalJwtDecoder: OriginalJwtDecoder {
    override fun decode(accessToken: String): UserRecord {
        return UserRecord(subject = "", username = "")
    }
}

class SpyOriginalJwtDecoder: OriginalJwtDecoder {
    var decode_argument_accessToken: String? = null
    override fun decode(accessToken: String): UserRecord {
        decode_argument_accessToken = accessToken
        return UserRecord(subject = "", username = "")
    }
}

class StubOriginalJwtDecoder: OriginalJwtDecoder {
    var decode_returnValue: UserRecord = UserRecord(subject = "", username = "")
    override fun decode(accessToken: String): UserRecord {
        return decode_returnValue
    }
}

class AlwaysErrorOriginalJwtDecoder: OriginalJwtDecoder {
    override fun decode(accessToken: String): UserRecord {
        throw java.security.SignatureException()
    }
}