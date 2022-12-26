package emesday.klate

import emesday.klate.exceptions.*
import emesday.klate.templates.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.jetbrains.exposed.sql.*


abstract class KlateModule {

    private var configured = false

    open val applicationModules: List<Application.() -> Unit> = emptyList()

    open val routingModules: List<Route.() -> Unit> = emptyList()

    @KtorDsl
    fun application(block: Application.() -> Unit): Application.() -> Unit = block

    @KtorDsl
    fun routing(block: Route.() -> Unit): Route.() -> Unit = block

    fun configure(application: Application) {
        checkNotConfigured()
        for (module in applicationModules) {
            module(application)
        }
        for (module in routingModules) {
            application.routing(module)

        }
//        configureApplication(application)
//        configureRouting(application)
        configured = true
    }

//    private fun configureApplication(application: Application) {
//        val callable = javaClass.kotlin.members.find { it.name == "application" }!!
//        val methods = javaClass.kotlin.members.filter {
//            it != callable && callable.returnType == it.returnType
//        }
//        methods.forEach {
//            @Suppress("UNCHECKED_CAST")
//            application.apply(it.call(this) as (Application.() -> Unit))
//        }
//    }
//
//    private fun configureRouting(application: Application) {
//        val callable = javaClass.kotlin.members.find { it.name == "routing" }!!
//        val methods = javaClass.kotlin.members.filter {
//            it != callable && callable.returnType == it.returnType
//        }
//        methods.forEach {
//            @Suppress("UNCHECKED_CAST")
//            application.routing {
//                (it.call(this@KlateModule) as (Route.() -> Unit))()
//            }
//        }
//    }

    private fun checkNotConfigured() {
        check(!configured) {
            "KlateModule already initialized."
        }
    }
}

abstract class BaseView : KlateModule() {

    open var templateFolder: String = "templates"

    open var staticFolder: String = "static"

    open var basePermission: List<String> = emptyList()

    open var defaultView: String = "list"

    fun ApplicationCall.getRedirect(): String {
        return ""
    }

    fun ApplicationCall.updateRedirect() {
    }

    fun ApplicationCall.flash(message: String, category: String = "message") {
    }
}

abstract class BaseModelView(
    datamodel: Table,
) : BaseView()

abstract class BaseCRUDView(datamodel: Table) : BaseModelView(datamodel) {

    val listTitle = ""

    val listTemplate = template {
        emesday.klate.templates.ab.Index()
    }

    // -----------------------------------------------------
    //         CRUD functions behaviour
    // -----------------------------------------------------

    /**
     * list function logic, override to implement different logic
     * returns list and search widget
     */
    fun ApplicationCall.list() {
        val name = javaClass.name
        val (orderColumn, orderDirection) = orderArgs.getOrElse(name) {
            Pair("", "")
        }
        val page = pageArgs[name]
        val pageSize = pageSizeArgs[name]
    }
}

abstract class RestCRUDView(datamodel: Table) : BaseCRUDView(datamodel)


/**
 * This is the CRUD generic view.
 * If you want to automatically implement create, edit,
 * delete, show, and list from your database tables,
 * inherit your views from this class.
 *
 * Notice that this class inherits from BaseCRUDView and BaseModelView
 * so all properties from the parent class can be overridden.
 */
abstract class ModelView(datamodel: Table) : RestCRUDView(datamodel) {

    /**
     * Override this function to control the
     * redirect after add endpoint is called.
     */
    open suspend fun ApplicationCall.postAddRedirect() {
        return respondRedirect(getRedirect())
    }

    /**
     * Override this function to control the
     * redirect after edit endpoint is called.
     */
    open suspend fun ApplicationCall.postEditRedirect() {
        return respondRedirect(getRedirect())
    }

    /**
     * Override this function to control the
     * redirect after edit endpoint is called.
     */
    open suspend fun ApplicationCall.postDeleteRedirect() {
        return respondRedirect(getRedirect())
    }


    val list = routing {
        get("/list/") {
            with(call) {
                updateRedirect()
                try {
                    val widgets = list()
                    respondKlateTemplate(listTemplate, mapOf("title" to listTitle, "widgets" to widgets))
                } catch (ex: KlateException) {
                    flash("An error occurred: ${ex.message}", "warning")
                    respondRedirect(getRedirect())
                }
            }
        }
    }

    val show = routing {
    }

    val add = routing {
    }

    val edit = routing {
    }

    val delete = routing {
    }

    val download = routing {
    }

    override val routingModules: List<Route.() -> Unit> =
        super.routingModules + listOf(list, show, add, edit, delete, download)
}


fun Application.configure(view: BaseView) {
    view.configure(this)
}
