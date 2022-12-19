package emesday.klate.templates.ab

import emesday.klate.templates.*
import emesday.klate.templates.ab.general.*
import io.ktor.server.html.*
import kotlinx.html.*

abstract class BaseLayout : Init() {

    val confirm = TemplatePlaceholder<Confirm>()
    val alert = TemplatePlaceholder<Alert>()
    val navbar = Placeholder<FlowContent>()
    val navbar1 = TemplatePlaceholder<Navbar>()
    val messages = Placeholder<FlowContent>()
    val flash = TemplatePlaceholder<Flash>()
    val content = Placeholder<FlowContent>()
    val footer = Placeholder<FlowContent>()
    val footer1 = TemplatePlaceholder<Footer>()

    override fun template(ctx: KlateContext, outer: HTML) = with(outer) {
        super.template(ctx, outer)
        body {
            insert(Confirm(ctx), confirm)
            insert(Alert(ctx), alert)
            insert(navbar) {
                header("top") {
                    role = "header"
                    insert(Navbar(ctx), navbar1)
                }
            }
            div("container") {
                div("row") {
                    insert(messages) {
                        insert(Flash(ctx), flash)
                    }
                    insert(content)
                }
            }
            insert(footer) {
                footer {
                    div("img-rounded nav-fixed-bottom") {
                        div("container") {
                            insert(Footer(ctx), footer1)
                        }
                    }
                }
            }
        }
    }
}