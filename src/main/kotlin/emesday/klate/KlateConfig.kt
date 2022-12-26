package emesday.klate

import emesday.klate.config.*
import emesday.klate.menu.*
import emesday.klate.security.*
import emesday.klate.security.defaults.*
import emesday.klate.view.*
import io.ktor.server.application.*
import kotlin.reflect.full.*

class KlateConfig {

    lateinit var application: Application

    var securityManager: BaseSecurityManager<
            out User<Role<PermissionView<Permission, ViewMenu>>>,
            out Role<PermissionView<Permission, ViewMenu>>,
            out Permission,
            out ViewMenu,
            out PermissionView<Permission, ViewMenu>
            >? = null
        get() {
            val securityManager = field ?: with(application) {
                DefaultSecurityManager(this)
            }
            field = securityManager
            return field
        }

    var indexView: BaseView? = null
        get() {
            field = field ?: with(application) {
                ac.klate.app.indexView?.let {
                    Class.forName(it).kotlin.createInstance() as BaseView
                } ?: IndexView()
            }
            return field
        }

    var templateBasePackagePath: String? = null
        get() = field ?: application.ac.klate.app.templateBasePackagePath ?: "templates"

    var menu: Menu? = null
}