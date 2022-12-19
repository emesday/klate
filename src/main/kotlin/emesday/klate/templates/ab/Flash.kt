package emesday.klate.templates.ab

import emesday.klate.templates.*
import io.ktor.server.html.*
import kotlinx.html.*

class Flash(val ctx: KlateContext) : Template<FlowContent> {

    override fun FlowContent.apply() {
        ctx.getFlashedMessagesWithCategory()?.let {
            for ((category, m) in it) {
                val additionalClass =if (category != null) {
                    "alert-$category"
                } else {
                    "alert-info"
                }
                div("alert $additionalClass") {
                    button {
                        type = ButtonType.button
                        classes = setOf("close")
                        attributes["data-dismiss"] = "alert"
                        +"&times"
                    }
                    +m
                }
            }
        }
    }
}