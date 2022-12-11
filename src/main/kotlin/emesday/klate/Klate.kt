package emesday.klate

import emesday.klate.database.*
import emesday.klate.security.*
import emesday.klate.view.*
import freemarker.cache.*
import freemarker.core.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.util.*
import java.io.*

class Klate(
    val application: Application,
    val securityManager: BaseSecurityManager,
    val indexView: BaseView
) {

    companion object Plugin : BaseApplicationPlugin<Application, KlateConfig, Klate> {

        override val key: AttributeKey<Klate> = AttributeKey("Klate")

        override fun install(pipeline: Application, configure: KlateConfig.() -> Unit): Klate {
            val builder = KlateConfig(pipeline).apply(configure)

            with(pipeline) {
                configureDatabase()

                install(FreeMarker) {
                    templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
                    outputFormat = HTMLOutputFormat.INSTANCE

                }
            }

            return Klate(
                application = pipeline,
                securityManager = builder.securityManager!!,
                indexView = builder.indexView!!
            )
        }
    }
}
