package emesday.klate

import emesday.klate.security.*
import emesday.klate.security.defaults.*
import io.ktor.server.application.*

class KlateConfig(val application: Application) {

    var securityManager: BaseSecurityManager? = null
        get() {
            val securityManager = field ?: DefaultSecurityManager(application)
            field = securityManager
            return field
        }
}