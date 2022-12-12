package emesday.klate.security.views

import emesday.klate.*
import emesday.klate.config.*
import emesday.klate.security.*
import emesday.klate.security.forms.*
import emesday.klate.view.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
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

    final override fun Route.routing() = routing {
        get("/login/") {
            login()
        }

        authenticate("auth-form") {
            post("/login/") {
                call.respondText("Hello, ${call.principal<UserIdPrincipal>()?.name}!")
            }
        }

        authenticate("auth-session") {
            get("/hello") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count + 1))
                call.respondText("Hello, ${userSession?.username}! Visit count is ${userSession?.count}.")
            }
        }

        get("/logout/") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }
    }

    override fun initialize(application: Application): Unit = with(application) {
        authentication {
            session<UserSession>("auth-session") {
                validate { session ->
                    null
                }
                challenge {
                    call.respondRedirect("/login/")
                }
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
                klate.securityManager
            }
        }
        return call.respond(FreeMarkerContent(loginTemplate, mapOf(
            "title" to title,
            "form" to form,
            "klate" to application.environment.config.klate
        )))
    }

    override fun initialize(application: Application): Unit = with(application) {
        super.initialize(application)
        authentication {
            form("auth-form") {
                userParamName = "username"
                passwordParamName = "password"
                validate { credentials ->
                    if (credentials.name == "jetbrains" && credentials.password == "foobar") {
                        UserIdPrincipal(credentials.name)
                    } else {
                        null
                    }
                }
                challenge {
                    call.respondRedirect("/login/")
                }
            }
        }
    }
}