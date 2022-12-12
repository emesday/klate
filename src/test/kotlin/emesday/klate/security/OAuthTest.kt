package emesday.klate.security

import emesday.klate.*
import emesday.klate.config.*
import emesday.klate.security.defaults.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import org.jetbrains.exposed.sql.transactions.*
import org.junit.*
import kotlin.test.*
import kotlin.test.Test

class OAuthTest {

    private val userInfoAlice = UserInfo(
        username = "alice",
        firstName = "Alice",
        lastName = "Doe",
        email = "alice@example.com",
        roleKeys = listOf("GROUP_1", "GROUP_2")
    )

    companion object {
        @BeforeClass
        @JvmStatic
        fun `add test users`() = testApplication {
            klateEnvironment {
                klate.auth.type = AuthType.OAUTH
            }
            application {
                install(Klate)
                klate.securityManager.addTestUsersAndRoles()
                klate.securityManager.assertOnlyDefaultUsers()
            }
        }
    }

    @Test
    fun `test login flow for - inactive user`() = testApplication {
        klateEnvironment {
            klate.auth.type = AuthType.OAUTH
        }
        application {
            install(Klate)

            assert(klate.securityManager is DefaultSecurityManager)
            assertEquals(AuthType.OAUTH, klate.securityManager.authType)

            // validate - no users are registered
            klate.securityManager.assertOnlyDefaultUsers()

            // register a user
            val newUser = klate.securityManager.addUser(
                username = userInfoAlice.username!!,
                firstName = userInfoAlice.firstName!!,
                lastName = userInfoAlice.lastName!!,
                email = userInfoAlice.email!!,
                roles = emptyList(),
            )

            assertNotNull(newUser)

            // validate - user was registered
            klate.securityManager.withUsers {
                assertEquals(3, count())
            }

            // set user inactive
            newUser.mutate {
                active = false
            }

            // attempt login
            val user = klate.securityManager.authUserOAuth(userInfoAlice)

            // validate - user was not allowed to log in
            assertNull(user)
        }
    }

    @Test
    fun `test login flow for - missing credentials`() = testApplication {
        klateEnvironment {
            klate.auth.type = AuthType.OAUTH
        }
        application {
            install(Klate)

            assert(klate.securityManager is DefaultSecurityManager)
            assertEquals(AuthType.OAUTH, klate.securityManager.authType)

            // validate - no users are registered
            klate.securityManager.assertOnlyDefaultUsers()

            // create userinfo with missing info
            val userInfoMissing = userInfoAlice.copy(username = null)

            // attempt login
            val user = klate.securityManager.authUserOAuth(userInfoMissing)

            // validate - login failure (missing username)
            assertNull(user)

            // validate - no users were created
            klate.securityManager.assertOnlyDefaultUsers()
        }
    }

    @Test
    fun `test login flow for - unregistered user`() = testApplication {
        klateEnvironment {
            klate.auth.type = AuthType.OAUTH
            klate.auth.user.registration = true
            klate.auth.user.registrationRole = "Public"
        }
        application {
            install(Klate)

            // validate - no users are registered
            klate.securityManager.assertOnlyDefaultUsers()

            // attempt login
            val user = klate.securityManager.authUserOAuth(userInfoAlice)

            // validate - user was allowed to log in
            assertNotNull(user)

            // validate - user was registered
            klate.securityManager.withUsers {
                assertEquals(3, count())
            }

            // validate - user was given the AUTH_USER_REGISTRATION_ROLE role
            transaction {
                assertEquals(listOfNotNull(klate.securityManager.findRole("Public")), user.roles)
            }

            // validate - user was given the correct attributes
            assertEquals("Alice", user.firstName, "Alice")
            assertEquals("Doe", user.lastName, "Doe")
            assertEquals("alice@example.com", user.email)
        }
    }

    @Test
    fun `test login flow for - unregistered user - no self-registration`() = testApplication {
        klateEnvironment {
            klate.auth.type = AuthType.OAUTH
            klate.auth.user.registration = false
        }

        application {
            install(Klate)

            // validate - no users are registered
            klate.securityManager.assertOnlyDefaultUsers()

            // attempt login
            val user = klate.securityManager.authUserOAuth(userInfoAlice)

            // validate - user was not allowed to log in
            assertNull(user)

            // validate - no users were registered
            klate.securityManager.assertOnlyDefaultUsers()
        }
    }

    @Test
    fun `test login flow for - unregistered user - single role mapping`() = testApplication {
        klateEnvironment {
            klate.auth.type = AuthType.OAUTH
            klate.auth.roles.mapping = mapOf(
                "GROUP_1" to listOf("Admin"),
                "GROUP_2" to listOf("User"),
            )
            klate.auth.user.registration = true
            klate.auth.user.registrationRole = "Public"
        }
        application {
            install(Klate)

            // add User role
            klate.securityManager.addRole("User")

            // validate - no users are registered
            klate.securityManager.assertOnlyDefaultUsers()

            // attempt login
            val user = klate.securityManager.authUserOAuth(userInfoAlice)

            // validate - user was allowed to log in
            assertNotNull(user)

            // validate - user was registered
            klate.securityManager.withUsers {
                assertEquals(3, count())
            }

            // validate - user was given the correct roles
            transaction {
                assertContentEquals(
                    listOf("Admin", "Public", "User").mapNotNull { klate.securityManager.findRole(it) }.sortedBy { it.name },
                    user.roles.toSet().sortedBy { it.name },
                )
            }

            // validate - user was given the correct attributes (read from LDAP)
            assertEquals("Alice", user.firstName)
            assertEquals("Doe", user.lastName)
            assertEquals("alice@example.com", user.email)
        }
    }

    @Test
    fun `test login flow for - unregistered user - multi role mapping`() = testApplication {
        klateEnvironment {
            klate.auth.type = AuthType.OAUTH
            klate.auth.roles.mapping = mapOf(
                "GROUP_1" to listOf("Admin", "User")
            )
            klate.auth.user.registration = true
            klate.auth.user.registrationRole = "Public"
        }
        application {
            install(Klate)

            // add User role
            klate.securityManager.addRole("User")

            // validate - no users are registered
            klate.securityManager.assertOnlyDefaultUsers()

            // attempt login
            val user = klate.securityManager.authUserOAuth(userInfoAlice)

            // validate - user was allowed to log in
            assertNotNull(user)

            // validate - user was registered
            klate.securityManager.withUsers {
                assertEquals(3, count())
            }

            // validate - user was given the correct roles
            transaction {
                assertContentEquals(
                    listOf("Admin", "Public", "User").mapNotNull { klate.securityManager.findRole(it) }.sortedBy { it.name },
                    user.roles.toSet().sortedBy { it.name },
                )
            }

            // validate - user was given the correct attributes (read from LDAP)
            assertEquals("Alice", user.firstName)
            assertEquals("Doe", user.lastName)
            assertEquals("alice@example.com", user.email)
        }
    }

    /** JMESPATH
     * def test__unregistered__jmespath_role(self):
     *     """
     *     OAUTH: test login flow for - unregistered user - jmespath registration role
     *     """
     *     self.app.config["AUTH_USER_REGISTRATION"] = True
     *     self.app.config[
     *     "AUTH_USER_REGISTRATION_ROLE_JMESPATH"
     *     ] = "contains(['alice'], username) && 'User' || 'Public'"
     *     self.appbuilder = AppBuilder(self.app, self.db.session)
     *     sm = self.appbuilder.sm

     *     # add User role
     *     sm.add_role("User")

     *     # validate - no users are registered
     *     self.assertOnlyDefaultUsers()

     *     # attempt login
     *     user = sm.auth_user_oauth(self.userinfo_alice)

     *     # validate - user was allowed to log in
     *     self.assertIsInstance(user, sm.user_model)

     *     # validate - user was registered
     *     self.assertEqual(len(sm.get_all_users()), 3)

     *     # validate - user was given the correct roles
     *     self.assertListEqual(user.roles, [sm.find_role("User")])

     *     # validate - user was given the correct attributes (read from LDAP)
     *     self.assertEqual(user.first_name, "Alice")
     *     self.assertEqual(user.last_name, "Doe")
     *     self.assertEqual(user.email, "alice@example.com")
     */

    @Test
    fun `test login flow for - registered user - multi role mapping - no login role-sync`() = testApplication {
        klateEnvironment {
            klate.auth.type = AuthType.OAUTH
            klate.auth.roles.mapping = mapOf(
                "GROUP_1" to listOf("Admin", "User")
            )
            klate.auth.roles.syncAtLogin = false
        }
        application {
            install(Klate)

            // add User role
            klate.securityManager.addRole("User")

            // validate - no users are registered
            klate.securityManager.assertOnlyDefaultUsers()

            // register a user
            val newUser = klate.securityManager.addUser(
                username = userInfoAlice.username!!,
                firstName = userInfoAlice.firstName!!,
                lastName = userInfoAlice.lastName!!,
                email = userInfoAlice.email!!,
                roles = emptyList(),
            )

            // validate - user was registered
            klate.securityManager.withUsers {
                assertEquals(3, count())
            }

            // attempt login
            val user = klate.securityManager.authUserOAuth(userInfoAlice)

            // validate - user was allowed to log in
            assertNotNull(user)

            // validate - user was given no roles
            transaction {
                assert(user.roles.count() == 0)
            }
        }
    }

    @Test
    fun `test login flow for - registered user - multi role mapping - with login role-sync`() = testApplication {
        klateEnvironment {
            klate.auth.type = AuthType.OAUTH
            klate.auth.roles.mapping = mapOf(
                "GROUP_1" to listOf("Admin", "User")
            )
            klate.auth.roles.syncAtLogin = true
        }
        application {
            install(Klate)

            // add User role
            klate.securityManager.addRole("User")

            // validate - no users are registered
            klate.securityManager.assertOnlyDefaultUsers()

            // register a user
            val newUser = klate.securityManager.addUser(
                username = userInfoAlice.username!!,
                firstName = userInfoAlice.firstName!!,
                lastName = userInfoAlice.lastName!!,
                email = userInfoAlice.email!!,
                roles = emptyList(),
            )


            // validate - user was registered
            klate.securityManager.withUsers {
                assertEquals(3, count())
            }


            // attempt login
            val user = klate.securityManager.authUserOAuth(userInfoAlice)

            // validate - user was allowed to log in
            assertNotNull(user)

            // validate - user was given the correct roles
            transaction {
                assertContentEquals(
                    listOf("Admin", "User").mapNotNull { klate.securityManager.findRole(it) }.sortedBy { it.name },
                    user.roles.toSet().sortedBy { it.name },
                )
            }
        }
    }

    @Test
    fun `test login flow for - registered user - jmespath registration role - no login role-sync`() = testApplication {
//        self.app.config["AUTH_ROLES_SYNC_AT_LOGIN"] = False
//        self.app.config["AUTH_USER_REGISTRATION"] = True
//        self.app.config[
//                "AUTH_USER_REGISTRATION_ROLE_JMESPATH"
//        ] = "contains(['alice'], username) && 'User' || 'Public'"
//        self.appbuilder = AppBuilder(self.app, self.db.session)
//        sm = self.appbuilder.sm
//
//        // add User role
//        sm.add_role("User")
//
//        // validate - no users are registered
//            self.assertOnlyDefaultUsers()
//
//        // register a user
//        new_user = sm.add_user(// noqa
//                username = "alice",
//        first_name = "Alice",
//        last_name = "Doe",
//        email = "alice@example.com",
//        role = [],
//        )
//
//        // validate - user was registered
//        self.assertEqual(len(sm.get_all_users()), 3)
//
//        // attempt login
//            user = sm.auth_user_oauth(self.userinfo_alice)
//
//        // validate - user was allowed to log in
//            self.assertIsInstance(user, sm.user_model)
//
//        // validate - user was given no roles
//        self.assertListEqual(user.roles, [])
    }

    @Test
    fun `test login flow for - registered user - jmespath registration role - with login role-sync`() =
        testApplication {
//        self.app.config["AUTH_ROLES_SYNC_AT_LOGIN"] = True
//        self.app.config["AUTH_USER_REGISTRATION"] = True
//        self.app.config[
//                "AUTH_USER_REGISTRATION_ROLE_JMESPATH"
//        ] = "contains(['alice'], username) && 'User' || 'Public'"
//        self.appbuilder = AppBuilder(self.app, self.db.session)
//        sm = self.appbuilder.sm
//
//        // add User role
//        sm.add_role("User")
//
//        // validate - no users are registered
//                self.assertOnlyDefaultUsers()
//
//        // register a user
//        new_user = sm.add_user(  // noqa
//                username="alice",
//        first_name="Alice",
//        last_name="Doe",
//        email="alice@example.com",
//        role=[],
//        )
//
//        // validate - user was registered
//        self.assertEqual(len(sm.get_all_users()), 3)
//
//        // attempt login
//                user = sm.auth_user_oauth(self.userinfo_alice)
//
//        // validate - user was allowed to log in
//                self.assertIsInstance(user, sm.user_model)
//
//        // validate - user was given the correct roles
//                self.assertListEqual(user.roles, [sm.find_role("User")])
        }


    @AfterTest
    fun afterTest() = testApplication {
        application {
            install(Klate)

            val role = klate.securityManager.findRole("User")
            transaction {
                if (role != null && role is Role) {
                    role.delete()
                }
            }

            klate.securityManager.findUser(userInfoAlice.username!!)?.let { user ->
                transaction {
                    if (user is User) {
                        user.delete()
                    }
                }
            }

            clearKlateApplicationConfig()
        }
    }
}

fun BaseSecurityManager<out UserItf<*>, out RoleItf, *, *, *>.assertOnlyDefaultUsers() {
    val userNames = withUsers {
        map { it.username }
    }
    assertEquals(listOf(USERNAME_ADMIN, USERNAME_READONLY), userNames)
}
