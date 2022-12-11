package emesday.klate.deprecated.appbuilder.ktor.menu

//import emesday.appbuilder.ktor.*
//import emesday.appbuilder.ktor.view.*
//import io.ktor.util.*

//@KtorDsl
//class Menu(
//    val app: AppBuilderConfiguration
//) {
//
//    val items: MutableList<MenuItem> = mutableListOf()
//}
//
//
//@KtorDsl
//fun Menu.category(
//    name: String,
//    icon: String? = null,
//    label: String? = null,
//    children: Category.() -> Unit
//) {
//    items.add(
//        CategoryMenuItem(
//            label ?: name,
//            Category(app).apply(children).result(),
//            icon
//        )
//    )
//}
//
//@KtorDsl
//fun Menu.link(
//    name: String,
//    href: String,
//    icon: String? = null,
//    label: String? = null,
//    baseView: View? = null,
//    cond: (() -> Boolean)? = null
//) {
//    items.add(
//        MenuItem2(
//            href,
//            (label ?: name),
//            icon,
//            cond
//        )
//    )
//}
//
//@KtorDsl
//fun Menu.view(
//    view: View,
//    name: String,
//    href: String? = null,
//    icon: String? = null,
//    label: String? = null,
//    cond: (() -> Boolean)? = null
//) {
//    app.addView(view)
//    items.add(
//        MenuItem2(
//            href ?: name,
//            (label ?: name),
//            icon,
//            cond
//        )
//    )
//}