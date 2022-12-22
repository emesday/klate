package emesday.klate.security.views

import emesday.klate.*
import emesday.klate.form.*
import emesday.klate.security.*
import emesday.klate.templates.*
import emesday.klate.templates.ab.general.security.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

abstract class AuthView : KlateView() {

    open val invalidLoginMessage: String = "Invalid login. Please try again."

    open val title: String = "Sign In"

    abstract suspend fun PipelineContext<Unit, ApplicationCall>.loginView()

    fun initializeSession() = application {
        install(Sessions) {
            cookie<UserSession>("user_session") {
                cookie.path = "/"
                cookie.maxAgeInSeconds = 60
            }
        }

        authentication {
            session<UserSession>("auth-session") {
                validate { session ->
                    if (session.username.startsWith("jet")) {
                        session
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

    fun route() = routing {
        get("/login/") {
            loginView()
        }

        authenticate("auth-form") {
            post("/login/") {
                val principal = call.principal<UserIdPrincipal>() ?: return@post call.respondRedirect("/login/")
                call.sessions.set(UserSession(principal.name, true, 0))
                call.respondRedirect("/hello")
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
}

class AuthDBView : AuthView() {

    val form = LoginForm

    override suspend fun PipelineContext<Unit, ApplicationCall>.loginView() {
        return call.respondKlateTemplate(LoginDB(form)) {
            title = this@AuthDBView.title
        }
    }

    fun auth() = application {
        authentication {
            form("auth-form") {
                userParamName = form.username.name
                passwordParamName = form.password.name
                validate { credentials ->
                    form.validate(this)
                    val securityManager = this@application.klate.securityManager
                    val user = securityManager.authUserDB(credentials.name, credentials.password)
                    user?.let { UserIdPrincipal(it.username) }
                }
                challenge {
                    println("challenge")
                    call.respondRedirect("/login/")
                }
            }
        }
    }
}