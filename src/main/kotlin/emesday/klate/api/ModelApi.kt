package emesday.klate.api

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.jetbrains.exposed.dao.id.*

open class ModelApi(table: IdTable<*>) : Api() {

    init {
        routable {
            get("_info") {
                info(this)
            }
            get("{pk}") {
                get(this)
            }
            get {
                getList(this)
            }
            post {
                post(this)
            }
            put("{pk}") {
                put(this)
            }
            delete("{pk}") {
                delete(this)
            }
        }
    }

    suspend fun info(
        pipeline: PipelineContext<Unit, ApplicationCall>,
    ) = with(pipeline) {
        println("info")
        call.respondText("info")
    }

    suspend fun get(
        pipeline: PipelineContext<Unit, ApplicationCall>,
    ) = with(pipeline) {
        call.respondText("get")
    }

    suspend fun getList(
        pipeline: PipelineContext<Unit, ApplicationCall>,
    ) = with(pipeline) {
        call.respondText("getList")
    }

    suspend fun post(
        pipeline: PipelineContext<Unit, ApplicationCall>,
    ) = with(pipeline) {
        call.respondText("post")
    }

    suspend fun put(
        pipeline: PipelineContext<Unit, ApplicationCall>,
    ) = with(pipeline) {
        call.respondText("put")
    }

    suspend fun delete(pipeline: PipelineContext<Unit, ApplicationCall>) = with(pipeline) {
        call.respondText("delete")
    }
}

fun createModelApi(
    table: IdTable<*>,
    builder: Api.() -> Unit = {},
): Api {
    return ModelApi(table).apply(builder)
}
