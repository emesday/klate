package emesday.klate.templates.ab

import emesday.klate.templates.*
import io.ktor.server.html.*
import kotlinx.html.*

class Footer(val ctx: KlateContext) : Template<FlowContent> {

    override fun FlowContent.apply() {
        div("row img-rounded") {
            hr()
            small {
                div("col-md-4") { }
                div("col-md-4") { }
                div("col-md-4") { }
            }
        }
    }
}