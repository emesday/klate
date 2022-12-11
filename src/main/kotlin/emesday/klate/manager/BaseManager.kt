package emesday.klate.manager

import emesday.klate.config.*
import io.ktor.server.application.*
import org.slf4j.*

open class BaseManager(val application: Application) {

    val klate: KlateApplicationConfig = application.environment.config.klate

    val log: Logger = LoggerFactory.getLogger(javaClass)

    fun preprocess() {
    }

    fun postprocess() {
    }
}