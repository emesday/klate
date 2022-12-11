package emesday.klate.deprecated.appbuilder
//
//import emesday.appbuilder.api.*
//import emesday.appbuilder.security.*
//import emesday.appbuilder.security.exposed.*
//import io.ktor.server.application.*
//import io.ktor.server.config.*
//import kotlin.reflect.*
//import kotlin.reflect.full.*
//
//data class Language(
//    val flag: String,
//    val name: String
//)
//
//class DatabaseSession
//
//@Suppress("UNCHECKED_CAST")
//fun <T> createInstance(name: String, vararg args: Any?): T {
//    return Class.forName(name).kotlin.primaryConstructor!!.call(*args) as T
//}
//
//class AppBuilder(
//    val app: Application,
//    session: DatabaseSession? = null,
//    menu: Menu? = null,
//    indexView: emesday.klate.deprecated.appbuilder.AbstractViewApi? = null,
//    var baseTemplate: String = "appbuilder/baselayout.ftl",
//    val staticFolder: String = "static/appbuilder",
//    val staticUrlPath: String = "/appbuilder",
//    securityManagerClass: String? = null,
//    val updatePerms: Boolean = true
//) {
//    fun <T : BaseApi> addApi(kClass: KClass<T>) {
//    }
//
//    fun <T : emesday.klate.deprecated.appbuilder.BaseView> addView(
//        kClass: KClass<T>,
//        name: String,
//        href: String? = null,
//        icon: String? = null,
//        label: String? = null,
//        category: String? = null,
//        categoryIcon: String? = null,
//        categoryLabel: String? = null,
//        menuCond: (() -> Boolean)? = null
//    ) {
//    }
//
//    val baseView = mutableListOf<emesday.klate.deprecated.appbuilder.AbstractViewApi>()
//
//    // :fab: _addon_managers
//    val addonManagerKeys = mutableListOf<String>()
//    val addonManagers = mutableMapOf<String, Any>()
//    val menu: Menu = menu ?: Menu()
//
//    // baseTemplate (constructor)
//    val securityManagerClass = securityManagerClass ?: ""
//    val indexView = indexView ?: emesday.klate.deprecated.appbuilder.DummyView()
//    // staticFolder (constructor)
//    // staticUrlPath (constructor)
//    // app (constructor)
//    // updatePerms (constructor)
//
//    private val config = app.environment.config
//
//    var sm: BaseSecurityManager
//
//    init {
//        val menu = config.tryGetString("kab.menu")?.let { createInstance<Menu>(it) } ?: Menu()
//        val updatePerms = config.tryGetString("kab.update_perms")?.let { it.toBoolean() } ?: true
//        val sm = config.tryGetString("kab.security_manager_class")
//            ?.let { createInstance<BaseSecurityManager>(it, this) } ?: SecurityManager(this)
//
//        // addons
//        // session
//        this.sm = sm
//        // bm
//        // openapi_manager
//        // menuapi_manager
//        // add_global_static
//        // add_global_filters
//        // app.before_request(self.sm.before_request)
//        // add_admin_views
//        // add_addon_views
//    }
//}
//
//private var appbuilderInstance: AppBuilder? = null
//
//var Application.appbuilder: AppBuilder
//    get() = appbuilderInstance!!
//    set(value) {
//        require(appbuilderInstance == null)
//        appbuilderInstance = value
//    }
