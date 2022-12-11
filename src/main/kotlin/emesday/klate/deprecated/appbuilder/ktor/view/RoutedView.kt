package emesday.klate.deprecated.appbuilder.ktor.view

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class RoutedView(val route: String, val view: View) {
    fun initialize(application: Application, root: String = "") {
        application.routing {
            get(route) {
                call.respond(FreeMarkerContent("simple.ftl", mapOf("item" to view)))
            }
        }
    }
}

