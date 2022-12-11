package emesday.klate.menu

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

class Menu(
    val application: Application,
) {

    init {
        application.routing {
            get("menu") {
                call.respondText("menu")
            }
        }
    }

    fun item(s: String, s1: String, s2: String, href: String) {
    }

    fun category(s: String, s1: String, s2: String, function: Menu.() -> Unit) {
    }

    fun separator() {
    }

    @Suppress("PublicApiImplicitType")
    companion object Plugin : BaseApplicationPlugin<Application, Menu, Menu> {

        override val key: AttributeKey<Menu> = AttributeKey("Menu")

        override fun install(pipeline: Application, configure: Menu.() -> Unit): Menu {
            return Menu(pipeline).apply(configure)
        }
    }
}

fun Application.menu(configuration: Menu.() -> Unit): Menu =
    pluginOrNull(Menu)?.apply(configuration) ?: install(Menu, configuration)
