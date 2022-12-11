package emesday.klate.deprecated.appbuilder.ktor.view

import io.ktor.server.application.*

abstract class View {

    var name: String? = "noname"

    var title: String? = null
        get() = field ?: "$name title"

    var content: String? = null
        get() = field ?: "$name content"
}

abstract class ModelView1(val application: Application)

fun createView() = {}