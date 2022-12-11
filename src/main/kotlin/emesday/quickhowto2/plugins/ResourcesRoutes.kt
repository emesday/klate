package emesday.quickhowto2.plugins

import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*

@Serializable
@Resource("/")
data class Hello(val name: String = "world")

@Serializable
@Resource("/links")
data class Page(val links: List<String> = emptyList())

fun Route.hello() {
    get<Hello> {
        call.respondText("""<a href="${application.href(Page())}">Hello ${it.name}!</a>""", ContentType.Text.Html)
    }
}

fun Route.links() {
    get<Page> {
        call.respondText("""<a href="${application.href(Hello())}">hello</a>""", ContentType.Text.Html)
    }
}