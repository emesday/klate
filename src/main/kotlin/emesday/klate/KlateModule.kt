package emesday.klate

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*

abstract class KlateModule {

    private var built = false

    open var routeBase: String? = null

    @KtorDsl
    fun application(block: Application.() -> Unit): Application.() -> Unit = block

    @KtorDsl
    fun routing(block: Route.() -> Unit): Route.() -> Unit = block

    fun configure(application: Application) {
        checkNotBuilt()
        configureApplication(application)
        configureRouting(application)
        built = true
    }

    private fun configureApplication(application: Application) {
        val callable = javaClass.kotlin.members.find { it.name == "application" }!!
        val methods = javaClass.kotlin.members.filter {
            it != callable && callable.returnType == it.returnType
        }
        methods.forEach {
            @Suppress("UNCHECKED_CAST")
            application.apply(it.call(this) as (Application.() -> Unit))
        }
    }

    private fun configureRouting(application: Application) {
        val callable = javaClass.kotlin.members.find { it.name == "routing" }!!
        val methods = javaClass.kotlin.members.filter {
            it != callable && callable.returnType == it.returnType
        }
        methods.forEach {
            @Suppress("UNCHECKED_CAST")
            application.routing {
                (it.call(this@KlateModule) as (Route.() -> Unit))()
            }
        }
    }

    private fun checkNotBuilt() {
        check(!built) {
            "KlateModule already initialized."
        }
    }
}

abstract class KlateView : KlateModule() {

    open var templateFolder: String = "templates"

    open var staticFolder: String = "static"

    open var basePermission: List<String> = emptyList()

    open var defaultView: String = "list"
}

fun Application.configure(view: KlateView) {
    view.configure(this)
}
