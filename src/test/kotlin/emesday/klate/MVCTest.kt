package emesday.klate

import emesday.klate.config.*
import emesday.klate.security.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import org.junit.*
import kotlin.test.*
import kotlin.test.Test

class MVCTest {

    val DEFAULT_INDEX_STRING = "Welcome"

    suspend fun HttpClient.browserLogin(username: String, password: String) {
        val loginUrl = "/login/"
        val x = post(loginUrl) {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(listOf("username" to username, "password" to password).formUrlEncode())
        }
        println(x.status)
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun `add test users`() = testApplication {
            klateEnvironment {
                klate.auth.type = AuthType.DB
            }
            application {
                install(Klate)
                klate.securityManager.addTestUsersAndRoles()
                klate.securityManager.assertOnlyDefaultUsers()
            }
        }
    }

    @Test
    fun `test views`() = testApplication {
        klateEnvironment()
        application {
            install(Klate)
            assertEquals(37, klate.baseViews.size)
        }
    }

    @Test
    fun `test back`() = testApplication {
        klateEnvironment()
        application {
            install(Klate)
        }
        client.browserLogin(USERNAME_ADMIN, PASSWORD_ADMIN)
        client.get("/model1view/list/?_flt_0_field_string=f")
        client.get("/model2view/list/")
        val respond = client.get("/back")
        println(respond)
    }

    @Test
    fun `test index`() = testApplication {
        klateEnvironment()
        application {
            install(Klate)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertContains(response.bodyAsText(), DEFAULT_INDEX_STRING)
    }
}