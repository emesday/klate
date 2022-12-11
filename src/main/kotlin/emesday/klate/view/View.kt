package emesday.klate.view

import emesday.klate.*

open class View : RoutableRoute() {

    var templateFolder: String = "templates"

    var staticFolder: String = "static"

    var basePermission: List<String> = emptyList()

    var defaultView: String = "list"
}

fun View.copyFrom(src: View): View = apply {
    (this as RoutableRoute).copyFrom(src)
    templateFolder = src.templateFolder
    staticFolder = src.staticFolder
    basePermission = src.basePermission
    defaultView = src.defaultView
}

fun createView(
    builder: View.() -> Unit = {},
): View {
    return View().apply(builder)
}

fun copyView(
    baseView: View,
    builder: View.() -> Unit = {},
): View {
    return View().copyFrom(baseView).apply(builder)
}
