package emesday.klate.view

import emesday.klate.*
import emesday.klate.templates.*
import emesday.klate.templates.ab.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun ApplicationCall.updateRedirect() {
//    val pageHistory = sessions.get("page_history")?.let { it as? List<*> } ?: emptyList<Any>()
}


open class IndexView : BaseView() {

    override var defaultView: String = "index"

    open val indexTemplate = template { Index() }

    val index = routing {
        get {
            call.updateRedirect()
            call.respondKlateTemplate(indexTemplate)
        }
    }

    override val routingModules: List<Route.() -> Unit> = listOf(index)
}
