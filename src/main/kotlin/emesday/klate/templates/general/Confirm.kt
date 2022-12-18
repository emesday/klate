package emesday.klate.templates.general

import emesday.klate.templates.*
import io.ktor.server.html.*
import kotlinx.html.*

class Confirm(val ctx: KlateContext) : Template<FlowContent> {

    override fun FlowContent.apply() {
        div("modal fade") {
            id = "modal-confirm"
            tabIndex = "-1"
            role = "dialog"

            div("modal-dialog modal-sm") {
                div("modal-content") {
                    div("modal-header") {
                        h4("modal-title") {
                            id = "myModalLabel"
                            ctx.i18n("User confirmation needed")
                        }
                    }
                    div("modal-body") {
                        div("modal-text")
                    }
                    div("modal-footer") {
                        button {
                            type = ButtonType.button
                            classes = setOf("btn", "btn-default")
                            unsafe {
                                +"data-dismiss=\"modal\""
                            }
                            +"OK"
                        }
                    }
                }
            }
        }
    }
}