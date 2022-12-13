package emesday.klate.security

import io.ktor.server.testing.*
import io.mockk.*
import java.time.*
import kotlin.test.*

data class MockUser(
    override var loginCount: Int,
    override var failLoginCount: Int,
    override var lastLogin: LocalDateTime?,
    override var active: Boolean,
    override var roles: Iterable<Role> = emptyList(),
    override var firstName: String = "",
    override var lastName: String = "",
    override var username: String = "",
    override var email: String = "",
) : User<Role>

class BaseSecurityManagerTest {

    @Test
    fun `first successful auth`() = testApplication {
        application {
            val bsm = mockk<BaseSecurityManager<User<Role>, Role, *, *, *>>()

            every { bsm.updateUserAuthStat(any(), any()) } answers { callOriginal() }
            every { bsm.updateUser(any()) } returns Unit

            val user = MockUser(0, 0, null, true)

            bsm.updateUserAuthStat(user, success = true)

            assertEquals(user.loginCount, 1)
            assertEquals(user.failLoginCount, 0)

            verify(exactly = 1) {
                bsm.updateUser(any())
            }
        }
    }

    @Test
    fun `first unsuccessful auth`() = testApplication {
        application {
            val bsm = mockk<BaseSecurityManager<User<Role>, Role, *, *, *>>()

            every { bsm.updateUserAuthStat(any(), any()) } answers { callOriginal() }
            every { bsm.updateUser(any()) } returns Unit

            val user = MockUser(0, 0, null, true)

            bsm.updateUserAuthStat(user, success = false)

            assertEquals(user.loginCount, 0)
            assertEquals(user.failLoginCount, 1)
            assertNull(user.lastLogin)

            verify(exactly = 1) {
                bsm.updateUser(any())
            }
        }
    }

    @Test
    fun `subsequent successful auth`() = testApplication {
        application {
            val bsm = mockk<BaseSecurityManager<User<Role>, Role, *, *, *>>()

            every { bsm.updateUserAuthStat(any(), any()) } answers { callOriginal() }
            every { bsm.updateUser(any()) } returns Unit

            val user = MockUser(5, 9, null, true)

            bsm.updateUserAuthStat(user, success = true)

            assertEquals(user.loginCount, 6)
            assertEquals(user.failLoginCount, 0)

            verify(exactly = 1) {
                bsm.updateUser(any())
            }
        }
    }

    @Test
    fun `subsequent unsuccessful auth`() = testApplication {
        application {
            val bsm = mockk<BaseSecurityManager<User<Role>, Role, *, *, *>>()

            every { bsm.updateUserAuthStat(any(), any()) } answers { callOriginal() }
            every { bsm.updateUser(any()) } returns Unit

            val user = MockUser(5, 9, null, true)

            bsm.updateUserAuthStat(user, success = false)

            assertEquals(user.loginCount, 5)
            assertEquals(user.failLoginCount, 10)
            assertNull(user.lastLogin)

            verify(exactly = 1) {
                bsm.updateUser(any())
            }
        }
    }
}