package emesday.quickhowto2

import emesday.klate.*
import emesday.klate.templates.*
import emesday.klate.templates.ab.*
import emesday.quickhowto2.dao.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.util.*

val hashKey = hex("6819b57a326945c1968f45236589")

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    DatabaseFactory.init()

//    install(DefaultHeaders)
//    install(CallLogging)
//    install(ConditionalHeaders)
//    install(PartialContent)
//    install(Resources)
//    install(Sessions) {
//        cookie<Session>("SESSION") {
//            transform(SessionTransportTransformerMessageAuthentication(hashKey))
//        }
//    }
//    install(ContentNegotiation) {
//        json()
//    }

    install(Klate)

//    routing {
//        hello()
//        links()
//
//        register(ContactApi)
//        register(ContactGroupApi)
//    }

//    menu {
//        item("databases", "Database Connections", "fa-database", "")
//        item("daum", "Daum", "fa-flask", "")
//        category("Data", "Data collections", "fa-databases") {
//            item("databases", "Database Connections", "fa-database", "")
//            separator()
//        }
//    }
}
