package emesday.klate.deprecated.appbuilder.ktor.menu

//import emesday.appbuilder.ktor.*
//import emesday.appbuilder.ktor.view.*
//import io.ktor.util.*
//
//@KtorDsl
//class Category(val app: AppBuilderConfiguration) {
//
//    val children: MutableList<MenuItem> = mutableListOf()
//
//    fun result(): List<MenuItem> = children.toList()
//}
//
//@KtorDsl
//fun Category.view(
//    view: View,
//    name: String,
//    href: String? = null,
//    icon: String? = null,
//    label: String? = null,
//    menuCond: (() -> Boolean)? = null
//) {
//    app.addView(view)
//    children.add(
//        MenuItem2(
//            href ?: name,
//            (label ?: name),
//            icon,
//            menuCond
//        )
//    )
//}
//
//@KtorDsl
//fun Category.link(
//    name: String,
//    href: String,
//    icon: String? = null,
//    label: String? = null,
//    baseView: View? = null,
//    cond: (() -> Boolean)? = null
//) {
//    children.add(
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
//fun Category.separator() {
//    children.add(seperatorMenuItem)
//}
