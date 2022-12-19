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

    open fun auth() = routing {
        get("/login/") {
            loginView()
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

    fun initializeSession() = application {
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

    override suspend fun PipelineContext<Unit, ApplicationCall>.loginView() {
        return call.respondKlateTemplate(LoginDB(LoginForm())) {
            title = this@AuthDBView.title
        }
    }

    fun initializeDBAuth() = application {
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