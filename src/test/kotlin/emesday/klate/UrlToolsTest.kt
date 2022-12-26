package emesday.klate

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.test.*

class UrlToolsTest {

    @Test
    fun `test orderArgs`() = testApplication {
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                get {
                    call.respond(call.orderArgs)
                }
            }
        }

        val response = client.get {
            parameter("_oc_myView", "col1")
            parameter("_od_myView", "desc")
        }

        val expected = Json.encodeToString(mapOf("myView" to Pair("col1", "desc")))
        val actual = response.bodyAsText()
        assertEquals(expected, actual)
    }

    @Test
    fun `test pageArgs and pageSizeArgs`() = testApplication {
        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                get("page") {
                    call.respond(call.pageArgs)
                }
                get("page_size") {
                    call.respond(call.pageSizeArgs)
                }
            }
        }

        val response = client.get {
            parameter("page_myView", "10")
            parameter("psize_myView", "10")
        }

        val expected = Json.encodeToString(mapOf("myView" to Pair("col1", "desc")))
        val actual = response.bodyAsText()
        assertEquals(expected, actual)
    }
}