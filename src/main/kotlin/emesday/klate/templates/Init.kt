package emesday.klate.templates

import emesday.klate.templates.general.*
import io.ktor.server.html.*
import kotlinx.html.*

class KlateContext {
    val appName: String = ""
    val title: String = ""
    fun static(path: String) = path
    fun i18n(text: String): String {
        return text
    }

    val appTheme: String? = null
}

/**
 * insert with default
 */
fun <TOuter> TOuter.insert(
    placeholder: Placeholder<TOuter>,
    meta: String = "",
    defaultContent: TOuter.(Placeholder<TOuter>) -> Unit,
): Unit = insert(placeholder.apply { invoke(meta, defaultContent) })

open class Init(val ctx: KlateContext) : Template<HTML> {

    val pageTitle = Placeholder<TITLE>()
    val headMeta = Placeholder<HEAD>()
    val headCss = Placeholder<HEAD>()
    val headJs = Placeholder<HEAD>()
    val body = Placeholder<FlowContent>()
    val tailJs = Placeholder<FlowContent>()
    val addTailJs = Placeholder<FlowContent>()
    val tail = Placeholder<FlowContent>()

    override fun HTML.apply() {
        head {
            title {
                insert(pageTitle) {
                    +ctx.appName
                }
            }
            insert(headMeta) {
                meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
                meta(name = "description", content = "")
                meta(name = "author", content = "")
            }
            insert(headCss) {
                link(href = ctx.static("css/bootstrap.min.css"), rel = "stylesheet")
                link(href = ctx.static("css/font-awesome.min.css"), rel = "stylesheet")
                if (ctx.appTheme != null) {
                    link(href = ctx.static("css/themes/${ctx.appTheme}"), rel = "stylesheet")
                }
                link(href = ctx.static("datepicker/bootstrap-datepicker.css"), rel = "stylesheet")
                link(href = ctx.static("select2/select2.css"), rel = "stylesheet")
                link(href = ctx.static("css/flags/flags16.css"), rel = "stylesheet")
                link(href = ctx.static("css/ab.css"), rel = "stylesheet")
            }
            insert(headJs) {
                script(src = ctx.static("js/jquery-latest.js")) {}
                script(src = ctx.static("js/ab_filters.js")) {}
                script(src = ctx.static("js/ab_actions.js")) {}
            }
        }
        body {
            insert(body)
            insert(tailJs) {
                script(src=ctx.static("js/bootstrap.min.js")) {}
                script(src=ctx.static("datepicker/bootstrap-datepicker.js")) {}
                script(src=ctx.static("select2/select2.js")) {}
                script(src=ctx.static("js/ab.js")) {}
            }
            insert(addTailJs)
            insert(tail)
        }
    }
}


open class BaseLayout(ctx: KlateContext) : Init(ctx) {

    val confirm = TemplatePlaceholder<Confirm>()

    init {
        body {
            insert(Confirm(ctx), confirm)
        }
    }
}
