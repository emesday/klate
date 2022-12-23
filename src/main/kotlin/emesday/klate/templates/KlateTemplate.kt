package emesday.klate.templates

import emesday.klate.*
import emesday.klate.exceptions.*
import emesday.klate.form.*
import emesday.klate.security.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import kotlinx.html.*

interface KlateTemplate : Template<HTML> {

    fun template(ctx: KlateContext, outer: HTML) {}

    override fun HTML.apply() {
        throw KlateException()
    }

    fun HTML.apply(ctx: KlateContext)
}

/**
 * insert with default
 */
fun <TOuter> TOuter.insert(
    placeholder: Placeholder<TOuter>,
    meta: String = "",
    defaultContent: TOuter.(Placeholder<TOuter>) -> Unit,
): Unit = insert(placeholder.apply { invoke(meta, defaultContent) })

fun FlowOrInteractiveOrPhrasingContent.render(
    renerable: Field,
    block: INPUT.() -> Unit = {},
) {
    renerable.render(this, block)
}

class MenuItem {

    val url: String = ""
    val name: String = ""
    val label: String = ""
    val icon: String? = null
    val visible: Boolean = true

    val children: List<MenuItem>? = null
}

class Menu {
    val extraClasses = ""
    val list = emptyList<MenuItem>()
}

class CurrentUser {
    val anonymous: Boolean = false
}

class Language {
    val flag = ""
    val name = ""
}

class KlateContext {

    val languages: Map<String, Language> = emptyMap()
    val session: Map<String, Any> = emptyMap()
    val logoutUrl: String = ""
    val userInfoUrl: String = ""
    val user: UserInfo = UserInfo()
    val loginUrl: String = ""
    val currentUser = CurrentUser()
    val registerUserUrl: String = ""

    val klate: KlatePluginInstance? = null

    val appName: String = ""
    val title: String = ""
    fun static(path: String) = "/static/appbuilder/$path"
    fun i18n(text: String): String {
        return text
    }

    fun getFlashedMessagesWithCategory(): List<Pair<String?, String>>? = null
    fun urlForLocale(lang: String): String = ""

    val appTheme: String? = null

    val appIcon: String? = null

    val menu: Menu = Menu()

    val indexUrl = "/"
}

suspend fun <TTemplate : KlateTemplate> ApplicationCall.respondKlateTemplate(
    template: TTemplate,
    status: HttpStatusCode = HttpStatusCode.OK,
    body: TTemplate.() -> Unit = {},
) {
    template.body()
    val ctx = klate.createContext()
    respondHtml(status) {
        with(template) {
            apply(ctx)
            template(ctx, this@respondHtml)
        }
    }
}
