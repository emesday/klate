package emesday.klate

import emesday.klate.config.*
import emesday.klate.database.*
import emesday.klate.freemarker.*
import emesday.klate.security.*
import emesday.klate.security.views.*
import emesday.klate.templates.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.util.*

class KlatePluginInstance(
    val securityManager: BaseSecurityManager<
            out User<Role<PermissionView<Permission, ViewMenu>>>,
            out Role<PermissionView<Permission, ViewMenu>>,
            out Permission,
            out ViewMenu,
            out PermissionView<Permission, ViewMenu>>,
    val indexView: KlateView,
    val config: KlateApplicationConfig,
) {

    val baseViews: MutableList<KlateView> = mutableListOf(AuthDBView())

    fun createContext(): KlateContext = KlateContext()
}

val klatePluginInstanceKey = AttributeKey<KlatePluginInstance>("KlatePluginInstance")

fun Application.configureStaticRoute() {
    routing {
        static("/static") {
            staticBasePackage = "static"
            resources(".")
        }
    }
}

val Klate = createApplicationPlugin("Klate", { KlateConfig() }) {
    val config = pluginConfig

    config.application = application
    with(application) {
        configureDatabase()
        configureStaticRoute()

        val klatePluginInstance = KlatePluginInstance(
            config.securityManager!!,
            config.indexView!!,
            environment.config.klate
        )

        configure(klatePluginInstance.indexView)
        for (view in klatePluginInstance.baseViews) {
            configure(view)
        }

        attributes.put(klatePluginInstanceKey, klatePluginInstance)

        val klateTemplateModel = KlateTemplateModel(this)
    }
}

val Application.klate: KlatePluginInstance
    get() = attributes[klatePluginInstanceKey]

val ApplicationCall.klate: KlatePluginInstance
    get() = application.klate


