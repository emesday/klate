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

    override fun routing(route: Route) {
        route {
            get {
                call.respond(
                    FreeMarkerContent(indexTemplate, Context("value"))
                )
            }
        }
    }
}


fun Application.createDefaultIndexView(): BaseView = with(this) {
    lateinit var view: BaseView
    routing {
        view = createView(this) {
            routeBase = ""

            defaultView = "index"

            routable {
                get {
                    call.respond(
                        FreeMarkerContent("appbuilder/index.ftl", Context("value"))
                    )
                }
            }
        }
    }
    return view
}

fun Application.createDefaultIndexView2(): BaseView = with(this) {
    lateinit var view: BaseView
    routing {

    }
    return view
}