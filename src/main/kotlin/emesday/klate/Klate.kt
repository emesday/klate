package emesday.klate

import emesday.klate.database.*
import emesday.klate.security.*
import emesday.klate.security.views.*
import emesday.klate.view.*
import freemarker.core.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.routing.*
import io.ktor.util.*

class KlatePluginInstance(
    val securityManager: BaseSecurityManager<
            out User<Role<PermissionView<Permission, ViewMenu>>>,
            out Role<PermissionView<Permission, ViewMenu>>,
            out Permission,
            out ViewMenu,
            out PermissionView<Permission, ViewMenu>>,
    val indexView: KlateView
) {

    val baseViews: MutableList<BaseView> = mutableListOf(AuthDBView())
}

val klatePluginInstanceKey = AttributeKey<KlatePluginInstance>("KlatePluginInstance")

val Klate = createApplicationPlugin("Klate", { KlateConfig() }) {
    val config = pluginConfig

    config.application = application
    with(application) {
        configureDatabase()
        install(FreeMarker) {
            templateLoader = config.templateLoader!!
            outputFormat = HTMLOutputFormat.INSTANCE
        }

        val klatePluginInstance = KlatePluginInstance(
            config.securityManager!!,
            config.indexView!!
        )

        configure(klatePluginInstance.indexView)
        routing {
            for (view in klatePluginInstance.baseViews) {
                configure(view)
            }
        }

        attributes.put(klatePluginInstanceKey, klatePluginInstance)
    }
}

val Application.klate: KlatePluginInstance
    get() = attributes[klatePluginInstanceKey]

val ApplicationCall.klate: KlatePluginInstance
    get() = application.klate
