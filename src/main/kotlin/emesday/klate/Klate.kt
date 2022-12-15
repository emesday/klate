package emesday.klate

import emesday.klate.config.*
import emesday.klate.database.*
import emesday.klate.freemarker.*
import emesday.klate.security.*
import emesday.klate.security.views.*
import freemarker.core.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
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
    val config: KlateApplicationConfig
) {

    val baseViews: MutableList<KlateView> = mutableListOf(AuthDBView())
}

val klatePluginInstanceKey = AttributeKey<KlatePluginInstance>("KlatePluginInstance")

val Klate = createApplicationPlugin("Klate", { KlateConfig() }) {
    val config = pluginConfig

    config.application = application
    with(application) {
        configureDatabase()

        routing {
            static("/static") {
                staticBasePackage = "static"
                resources(".")
            }
        }

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

        install(FreeMarker) {
            templateLoader = config.templateLoader!!
            outputFormat = HTMLOutputFormat.INSTANCE
            setSharedVariable("klate", klateTemplateModel)
        }
    }
}

val Application.klate: KlatePluginInstance
    get() = attributes[klatePluginInstanceKey]

val ApplicationCall.klate: KlatePluginInstance
    get() = application.klate


