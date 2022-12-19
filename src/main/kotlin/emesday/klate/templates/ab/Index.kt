package emesday.klate.templates.ab

import emesday.klate.templates.*
import kotlinx.html.*

class Index : Base() {

    override fun HTML.apply(ctx: KlateContext) {
        content {
            h2 {
                div {
                    style = "text-align: center;"
                    +ctx.i18n("Welcome")
                }
            }
            div("well")
        }
    }
}