package emesday.klate

import emesday.klate.config.*
import emesday.klate.view.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlin.test.*

class CustomIndexView : IndexView() {
    override val indexTemplate: String = "custom_index.ftl"
}

class CustomIndexViewTest {

    @Test
    fun `custom indexView`() = testApplication {
        klateEnvironment {
            klate.app.indexView = "emesday.klate.CustomIndexView"
        }
        application {
            install(Klate)
        }

        val response = client.get("")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("This is a custom index view.", response.bodyAsText())
    }
}