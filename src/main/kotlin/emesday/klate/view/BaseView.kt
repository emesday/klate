package emesday.klate.view

import emesday.klate.*
import io.ktor.server.routing.*

open class BaseView : RoutableRoute() {

    open var templateFolder: String = "templates"

    open var staticFolder: String = "static"

    open var basePermission: List<String> = emptyList()

    open var defaultView: String = "list"
}

fun BaseView.copyFrom(src: BaseView): BaseView = apply {
    (this as RoutableRoute).copyFrom(src)
    templateFolder = src.templateFolder
    staticFolder = src.staticFolder
    basePermission = src.basePermission
    defaultView = src.defaultView
}

fun createView(
    route: Route,
    builder: BaseView.() -> Unit = {},
): BaseView {
    val view = BaseView().apply(builder)
    route {
        view.register(this)
    }
    return view
}

fun copyView(
    baseView: BaseView,
    builder: BaseView.() -> Unit = {},
): BaseView {
    return BaseView().copyFrom(baseView).apply(builder)
}
