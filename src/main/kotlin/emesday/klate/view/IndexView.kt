package emesday.klate.view

import emesday.klate.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


val IndexView: View = createView {
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

val IndexView2: View = copyView(IndexView) {
    routeBase = "index2"
}
