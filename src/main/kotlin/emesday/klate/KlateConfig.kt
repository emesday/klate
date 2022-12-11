package emesday.klate

import emesday.klate.config.*
import emesday.klate.security.*
import emesday.klate.security.defaults.*
import emesday.klate.view.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.reflect.full.*

class KlateConfig(val application: Application) {

    val klate = application.environment.config.klate

    var securityManager: BaseSecurityManager? = null
        get() {
            val securityManager = field ?: with(application) {
                DefaultSecurityManager(this)
            }
            field = securityManager
            return field
        }

    var indexView: BaseView? = null
        get() {
            val view = field ?: with(application) {
                val view = klate.app.indexView?.let {
                    Class.forName(it).kotlin.createInstance() as BaseView
                } ?: IndexView()
                routing {
                    view.routing(this)
                }
                view
            }
            field = view
            return field
        }
}