package emesday.klate.security.config

import emesday.klate.config.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationConfigTest {

    private val mapping = mapOf(
        "k1" to listOf("v1", "v2"),
        "k2" to listOf("v3")
    )

    private val mapping2 = mapOf(
        "k1" to listOf("v1", "v2")
    )

    @Test
    fun `from application yaml`() = testApplication {
        application {
            assertEquals(
                mapOf("public" to listOf("public1", "public2")),
                environment.config.klate.auth.roles.mapping
            )
        }
    }

    @Test
    fun `cache test`() {
        val config = MapApplicationConfig()
        config.klate.auth.roles.mapping = mapping
        assertEquals(mapping, config.klate.auth.roles.mapping)

        cache["klate.auth.roles.mapping"] = mapping2
        assertEquals(mapping2, config.klate.auth.roles.mapping)
    }

    @AfterTest
    fun afterTest() {
        clearKlateApplicationConfig()
    }
}
