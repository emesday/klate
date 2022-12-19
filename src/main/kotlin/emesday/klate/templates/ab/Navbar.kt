package emesday.klate.templates.ab

import emesday.klate.templates.*
import io.ktor.server.html.*
import kotlinx.html.*

class Navbar(val ctx: KlateContext) : Template<FlowContent> {

    val navbarMenu = TemplatePlaceholder<NavbarMenu>()
    val navbarRight = TemplatePlaceholder<NavbarRight>()

    override fun FlowContent.apply() {
        div("navbar ${ctx.menu.extraClasses}") {
            role = "navigation"
            div("container") {
                div("navbar-header") {
                    button {
                        type = ButtonType.button
                        classes = setOf("navbar-toggle")
                        attributes["data-toggle"] = "collapse"
                        attributes["data-target"] = ".navbar-collapse"

                        span("icon-bar")
                        span("icon-bar")
                        span("icon-bar")
                    }
                    if (ctx.appIcon != null) {
                        a(ctx.indexUrl) {
                            classes = setOf("navbar-brand")
                            img(src=ctx.appIcon) {
                                height = "100%"
                                width = "auto"
                            }
                        }
                    } else {
                        span("navbar-brand") {
                            a(ctx.indexUrl) {
                                +ctx.appName
                            }
                        }
                    }
                }
                div("navbar-collapse collapse") {
                    ul("nav navbar-nav") {
                        insert(NavbarMenu(ctx), navbarMenu)
                    }
                    ul("nav navbar-nav navbar-right") {
                       insert(NavbarRight(ctx), navbarRight)
                    }
                }
            }
        }
    }
}