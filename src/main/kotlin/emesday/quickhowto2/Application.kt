package emesday.quickhowto2

import emesday.klate.*
import emesday.klate.menu.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Klate) {
        indexView = MyIndexView()
        menu = Menu(reverse = false)
        securityManager = MySecurityManager(application)
    }
    createViews()
}

fun Application.createViews() {
    with(klate) {
        addView(
            ::createGroupModelView,
            "List Groups",
            icon = "fa-folder-open-o",
            category="Contacts",
            categoryIcon="fa-envelope"
        )
    }
}