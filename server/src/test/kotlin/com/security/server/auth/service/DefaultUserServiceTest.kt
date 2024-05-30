package com.security.server.auth.service

import com.security.server.auth.UserRepository
import com.security.server.auth.authentication.SocialLoginUser
import com.security.server.auth.entity.UserRecord
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
        val userService = DefaultUserService(userRepository)


        val userResponse = userService.createOrGet(SocialLoginUser("subject2", ""))


        assertEquals("correct user", userResponse.name)
    }

    @Test
    fun ユーザーが存在していなければIDとユーザー名を保存する() {
        userRepository.deleteAll()
        val userService = DefaultUserService(userRepository)


        userService.createOrGet(SocialLoginUser("subject", "new user"))
        val userRecords = userRepository.findAll()


        assertEquals(1, userRecords.size)
        assertEquals("subject", userRecords.first().subject)
        assertEquals("new user", userRecords.first().username)
    }
}