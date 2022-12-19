package emesday.klate.view

import emesday.klate.*
import emesday.klate.templates.*
import emesday.klate.templates.ab.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

open class IndexView : KlateView() {

    override var defaultView: String = "index"

    open fun createIndexTemplate(): KlateTemplate = Index()

    fun index() = routing {
        get {
            call.respondKlateTemplate(createIndexTemplate())
        }
    }
}
