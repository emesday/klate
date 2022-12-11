package emesday.klate.security.views

import emesday.klate.*
import emesday.klate.config.*
import emesday.klate.security.*
import emesday.klate.security.forms.*
import emesday.klate.view.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

open class AuthView : BaseView() {

    override var routeBase: String? = ""

    open var loginTemplate: String = ""

    open val invalidLoginMessage: String = "Invalid login. Please try again."

    open val title: String = "Sign In"

    open suspend fun PipelineContext<Unit, ApplicationCall>.login() {
    }

    open suspend fun PipelineContext<Unit, ApplicationCall>.logout() {
    }

    override fun routing(route: Route) {
        route {
            get("/login/") {
                login()
            }
            post("/login/") {
                login()
            }
            get("/logout/") {
                logout()
            }
        }
    }
}

class AuthDBView : AuthView() {
    override var loginTemplate: String = "appbuilder/general/security/login_db.html"

    override suspend fun PipelineContext<Unit, ApplicationCall>.login() {
        val session = call.sessions.get<UserSession>()
        if (session != null && session.isAuthenticated) {
            return call.respondRedirect("index")
        }
        val form = LoginFormDB()
        if (call.request.httpMethod == HttpMethod.Post) {
            with(application) {
                securityManager
            }
        }
        return call.respond(FreeMarkerContent(loginTemplate, mapOf(
            "title" to title,
            "form" to form,
            "klate" to application.environment.config.klate
        )))
    }
}