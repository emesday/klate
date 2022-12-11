package emesday.klate.deprecated

import io.ktor.server.routing.*
import io.ktor.util.*

@KtorDsl
open class KlateConfig(val path: String) {

    var additionalRoute: (Route.() -> Unit)? = null

    @KtorDsl
    fun route(additionalRoute: Route.() -> Unit) {
        this.additionalRoute = additionalRoute
    }

    var listTemplate: String = "appbuilder/general/model/list.html"
}
