package com.security.server.auth.coder

import com.security.server.auth.entity.UserRecord

class DummyOriginalJwtEncoder: OriginalJwtEncoder {
    override fun encode(userRecord: UserRecord): String {
        return ""
    }
}

class SpyOriginalJwtEncoder: OriginalJwtEncoder {
    var encode_argument_userRecord: UserRecord? = null
    override fun encode(userRecord: UserRecord): String {
        encode_argument_userRecord = userRecord
        return ""
    }
}

class StubOriginalJwtEncoder: OriginalJwtEncoder {
    var encode_returnValue: String = ""
    override fun encode(userRecord: UserRecord): String {
        return encode_returnValue
    }
}