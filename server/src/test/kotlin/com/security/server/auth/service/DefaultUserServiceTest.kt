package com.security.server.auth.service

import com.security.server.auth.entity.UserRecord
import com.security.server.auth.UserRepository
import com.security.server.auth.coder.DummyUserRecordJwtEncoder
import com.security.server.auth.coder.SpyUserRecordJwtEncoder
import com.security.server.auth.coder.StubUserRecordJwtEncoder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

@DataJpaTest
class DefaultUserServiceTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun ユーザーがすでに存在していればIDとユーザー名をレスポンスに含めて返す() {
        val userRecords = listOf(
            UserRecord(
                UUID.randomUUID(),
                "subject1",
                "wrong user"
            ),
            UserRecord(
                UUID.randomUUID(),
                "subject2",
                "correct user"
            ),
        )
        userRepository.saveAll(userRecords)
        val userService = DefaultUserService(userRepository, DummyUserRecordJwtEncoder())


        val userResponse = userService.createOrGet("subject2", "")


        assertEquals("correct user", userResponse.name)
    }

    @Test
    fun ユーザーが存在していなければIDとユーザー名を保存する() {
        userRepository.deleteAll()
        val userService = DefaultUserService(userRepository, DummyUserRecordJwtEncoder())


        userService.createOrGet("subject", "new user")
        val userRecords = userRepository.findAll()


        assertEquals(1, userRecords.size)
        assertEquals("subject", userRecords.first().subject)
        assertEquals("new user", userRecords.first().username)
    }

    @Test
    fun JwtEncoderにユーザー情報を渡してアクセストークンを生成する() {
        val savedUserRecord = userRepository.save(
            UserRecord(UUID.randomUUID(), "subject", "saved user")
        )
        val spyJwtEncoder = SpyUserRecordJwtEncoder()
        val userService = DefaultUserService(userRepository, spyJwtEncoder)


        userService.createOrGet("subject", "new user")


        assertEquals(savedUserRecord.id, spyJwtEncoder.encode_argument_userRecord?.id)
        assertEquals("subject", spyJwtEncoder.encode_argument_userRecord?.subject)
        assertEquals("saved user", spyJwtEncoder.encode_argument_userRecord?.username)
    }

    @Test
    fun JwtEncoderによって生成されたアクセストークンを返す() {
        val stubJwtEncoder = StubUserRecordJwtEncoder()
        stubJwtEncoder.encode_returnValue = "access token"
        val userService = DefaultUserService(userRepository, stubJwtEncoder)


        val userResponse = userService.createOrGet("", "")


        assertEquals("access token", userResponse.accessToken)
    }
}