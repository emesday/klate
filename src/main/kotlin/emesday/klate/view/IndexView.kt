package emesday.klate.view

import emesday.klate.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

open class IndexView : BaseView() {

    override var routeBase: String? = ""

    override var defaultView: String = "index"

    open val indexTemplate = "appbuilder/index.ftl"

    override fun Route.routing() = routing {
        get {
            call.respond(
                FreeMarkerContent(indexTemplate, Context("value"))
            )
        }
    }
}
