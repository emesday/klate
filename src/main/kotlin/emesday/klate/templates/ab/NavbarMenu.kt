package emesday.klate.templates.ab

import emesday.klate.templates.*
import io.ktor.server.html.*
import kotlinx.html.*

class NavbarMenu(val ctx: KlateContext) : Template<UL> {

    private fun LI.menuItem(item: MenuItem) {
        a(item.url) {
            tabIndex = "-1"
            if (item.icon != null) {
                i("fa fa-fw ${item.icon}")
            }
            +ctx.i18n(item.label)
        }
    }

    override fun UL.apply() {
        for (item in ctx.menu.list) {
            if (item.visible) {
                if (item.children != null) {
                    li("dropdown") {
                        a("javascript:void(0)", classes = "dropdown-toggle") {
                            attributes["data-toggle"] = "dropdown"
                            if (item.icon != null) {
                                i("fa ${item.icon}")
                            }
                            +ctx.i18n(item.label)
                            b("caret")
                        }
                        ul("dropdown-menu") {
                            for ((idx, child) in item.children.withIndex()) {
                                if (child.name == "-" && idx != item.children.size - 1) {
                                    li("divider")
                                } else if (child.visible) {
                                    li item@ {
                                        this@item.menuItem(child)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    li {
                        this@li.menuItem(item)
                    }
                }
            }
        }
    }
}