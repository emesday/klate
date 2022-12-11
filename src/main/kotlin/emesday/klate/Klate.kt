package emesday.klate

import emesday.klate.database.*
import emesday.klate.security.*
import io.ktor.server.application.*
import io.ktor.util.*

class Klate(
    val application: Application,
    val securityManager: BaseSecurityManager,
) {

    companion object Plugin : BaseApplicationPlugin<Application, KlateConfig, Klate> {

        override val key: AttributeKey<Klate> = AttributeKey("Klate")

        override fun install(pipeline: Application, configure: KlateConfig.() -> Unit): Klate {
            val builder = KlateConfig(pipeline).apply(configure)

            with(pipeline) {
                configureDatabase()
            }

            return Klate(
                application = pipeline,
                securityManager = builder.securityManager!!
            )
        }
    }
}
