package emesday.klate.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*

@Serializable
data class DummyMenuData(val name: String)

val MenuApi = createApi {

    resourceName = "menu"

    openapiSpecTag = "Menu"

    routable {
        get {
            call.respond(DummyMenuData("hello"))
        }
    }
}