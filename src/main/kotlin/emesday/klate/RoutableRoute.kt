package emesday.klate

import io.ktor.server.routing.*
import io.ktor.util.*

open class RoutableRoute {
    val routableRoutes: MutableList<Route.() -> Unit> = mutableListOf()

    open var routeBase: String? = null
        get() = field ?: ""

    @KtorDsl
    fun routable(route: Route.() -> Unit) {
        routableRoutes.add(route)
    }

    fun register(route: Route): Route {
        return route.route(routeBase!!) {
            for (it in routableRoutes) {
                it()
            }
        }
    }

    operator fun invoke(route: Route): Route {
        return register(route)
    }
}

fun RoutableRoute.copyFrom(src: RoutableRoute): RoutableRoute =
    apply {
        routableRoutes.addAll(src.routableRoutes)
        routeBase = src.routeBase
    }

fun Route.register(baseView: RoutableRoute): Route {
    return baseView.register(this)
}
