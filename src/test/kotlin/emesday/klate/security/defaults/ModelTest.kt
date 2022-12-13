package emesday.klate.security.defaults

import emesday.klate.*
import emesday.klate.database.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import kotlin.test.*

class ModelTest {

    @Test
    fun `User - UserRoles - Role test`() = testApplication {
        environment {
            buildEnvironment()
        }

        application {
            configureDatabase()
            createDefaultSecurityManagerTables()

            val user = transaction {
                UserEntity.new {
                    firstName = ""
                    lastName = ""
                    username = "alice"
                    password = ""
                    active = true
                    email = "alice@example.com"
                    lastLogin = null
                    loginCount = 0
                    failLoginCount = 0
                }
            }
            val role = transaction {
                RoleEntity.new {
                    name = "Public"
                }
            }

            transaction {
                user.roles = SizedCollection(role)
            }

            transaction {
                assertContentEquals(listOf(role), user.roles)
            }
        }
    }
}