package emesday.quickhowto2

import emesday.klate.security.defaults.*
import io.ktor.server.application.*

class MySecurityManager(application: Application) : DefaultSecurityManager(application) {
}