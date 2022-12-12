package emesday.klate

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*

abstract class RoutableRoute {

    open var routeBase: String? = null
        get() = field ?: ""

    fun register(route: Route): Route = with(route) {
        initialize(application)
        route(routeBase!!) {
            routing()
        }
    }

    @KtorDsl
    protected fun Route.routing(body: Route.() -> Unit) = invoke { body() }

    abstract fun Route.routing()

    open fun initialize(application: Application) {}
}

fun Route.register(baseView: RoutableRoute): Route {
    return baseView.register(this)
}
