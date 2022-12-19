package emesday.klate.templates.ab.general

import emesday.klate.templates.*
import io.ktor.server.html.*
import kotlinx.html.*

class Alert(val ctx: KlateContext) : Template<FlowContent> {

    override fun FlowContent.apply() {
        div("modal fade") {
            id = "modal-alert"
            tabIndex = "-1"
            role = "dialog"

            div("modal-dialog modal-sm") {
                div("modal-content") {
                    div("modal-body") {
                        div {
                            h4("modal-text")
                        }
                    }
                    div("modal-footer") {
                        button {
                            type = ButtonType.button
                            classes = setOf("btn", "btn-default")
                            attributes["data-dismiss"] = "modal"
                            +"OK"
                        }
                    }
                }
            }
        }
    }
}