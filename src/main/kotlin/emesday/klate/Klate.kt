package emesday.klate

import emesday.klate.database.*
import emesday.klate.security.*
import emesday.klate.view.*
import freemarker.cache.*
import freemarker.core.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.util.*

val securityManagerKey = AttributeKey<BaseSecurityManager>("securityManager")

val indexViewKey = AttributeKey<BaseView>("indexView")

val baseViewsKey = AttributeKey<MutableList<BaseView>>("baseViews")

val Klate = createApplicationPlugin("Klate", { KlateConfig() }) {
    pluginConfig.application = application

    with(application) {
        configureDatabase()
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
            outputFormat = HTMLOutputFormat.INSTANCE

        }
    }

    application.attributes.put(securityManagerKey, pluginConfig.securityManager!!)
    application.attributes.put(indexViewKey, pluginConfig.indexView!!)
    application.attributes.put(baseViewsKey, mutableListOf())
}

val Application.securityManager: BaseSecurityManager
    get() = attributes[securityManagerKey]

val Application.baseViews: MutableList<BaseView>
    get() = attributes[baseViewsKey]

