package emesday.klate.api

import emesday.klate.*

open class Api : RoutableRoute() {

    var resourceName: String = javaClass.name.lowercase()

    var openapiSpecTag: String = ""

    var version: String = "v1"

    override var routeBase: String?
        get() = super.routeBase ?: "/api/${version}/${resourceName.lowercase()}"
        set(value) {
            super.routeBase = value
        }
}

fun createApi(
    builder: Api.() -> Unit = {},
): Api {
    return Api().apply(builder)
}