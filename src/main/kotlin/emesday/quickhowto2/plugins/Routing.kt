package emesday.quickhowto2.plugins

import freemarker.template.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

inline fun <reified T> String.convertTo(): T {
    return when (T::class) {
        Int::class -> toInt()
        Long::class -> toLong()
        else -> throw Error()
    } as T
}

//fun <T : Comparable<T>, M> Routing.addCrud(name: String, table: CrudTable<T, M>) {
//
//    val dao = DAOFacadeImpl(table)
//
//    route(name) {
//        get {
//            call.respond(FreeMarkerContent("index.ftl", mapOf("items" to dao.getAll())))
//        }
//        get("new") {
//            call.respond(FreeMarkerContent("new.ftl", model = null))
//        }
////    post {
////      val formParameters = call.receiveParameters()
////      val title = formParameters.getOrFail("title")
////      val body = formParameters.getOrFail("body")
////      val article = dao.create(title, body)
////      call.respondRedirect("/articles/${article?.id}")
////    }
//        get("{id}") {
//            val id = call.parameters.getOrFail("id").convertTo<T>()
//            call.respond(FreeMarkerContent("show.ftl", mapOf("item" to dao.get(id))))
//        }
//        get("{id}/edit") {
//            val id = call.parameters.getOrFail("id").convertTo<T>()
//            call.respond(FreeMarkerContent("edit.ftl", mapOf("item" to dao.get(id))))
//        }
////    post("{id}") {
////      val id = call.parameters.getOrFail("id").toIdType<T>()
////      val formParameters = call.receiveParameters()
////      when (formParameters.getOrFail("_action")) {
////        "update" -> {
////          val title = formParameters.getOrFail("title")
////          val body = formParameters.getOrFail("body")
////          dao.editArticle(id, title, body)
////          call.respondRedirect("/articles/$id")
////        }
////        "delete" -> {
////          dao.deleteArticle(id)
////          call.respondRedirect("/articles")
////        }
////      }
////    }
//    }
//
//}

class FlashedMessages : TemplateMethodModelEx {
    override fun exec(arguments: MutableList<Any?>): Any? {
        println(arguments)
        return listOf("cate" to "bang")
    }
}

class UrlFor : TemplateMethodModelEx {
    override fun exec(arguments: MutableList<Any?>): Any? {
        return if (arguments.size > 1 && arguments.first().toString() == "appbuilder.static") {
            "static/appbuilder/${arguments[1]}"
        } else if (arguments.size > 0) {
            arguments[0]
        } else {
            "nothing"
        }
    }
}

class UrlForLocale : TemplateMethodModelEx {
    override fun exec(arguments: MutableList<Any?>): Any? {
        return arguments[0]
    }
}

class GetList : TemplateMethodModelEx {
    override fun exec(arguments: MutableList<Any?>): Any? {
        return emptyList<String>()
    }
}

fun Application.configureRouting() {

    routing {
        static("/static") {
            resources("files")
        }
        get("/") {
            call.respond(
                FreeMarkerContent(
                    "appbuilder2/index.ftl",
                    mapOf(
                        "title" to "Hello",
                        "base_template" to "baselayout.ftl",
                        "get_flashed_messages" to FlashedMessages(),
                        "url_for" to UrlFor(),
                        "appbuilder" to mapOf(
                            "get_url_for_userinfo" to "/userinfo",
                            "get_url_for_login" to "/login",
                            "get_url_for_logout" to "/logout",
                            "get_url_for_index" to "/",
                            "get_url_for_locale" to UrlForLocale(),
                            "menu" to mapOf(
                                "extra_classes" to "",
                                "get_list" to GetList()
                            ),
                            "app_icon" to "",
                            "app_name" to "Awesome",
                            "languages" to emptyList<String>(),
                        )
                    )
                )
            )
        }
    }
}
