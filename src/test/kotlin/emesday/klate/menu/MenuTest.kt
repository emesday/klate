package emesday.klate.menu

import emesday.klate.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.test.*

class MenuTest {

    @Test
    fun `menu access denied`() = testApplication {
        klateEnvironment()
        application {
            install(Klate)
        }

        val response = client.get("/api/v1/menu")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}