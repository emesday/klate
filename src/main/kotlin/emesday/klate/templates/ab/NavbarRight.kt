package emesday.klate.templates.ab

import emesday.klate.templates.*
import io.ktor.server.html.*
import kotlinx.html.*

class NavbarRight(val ctx: KlateContext) : Template<UL> {

    override fun UL.apply() {
        val locale = ctx.session["locale"] ?: "en"
        if (ctx.languages.keys.size > 1) {
            li("dropdown") {
                a("javascript:void(0)", classes = "dropdown-toggle") {
                    attributes["data-toggle"] = "dropdown"
                    div("f16") {
                        i("flag ${ctx.languages[locale]?.flag}")
                        b("caret")
                    }
                }
                ul("dropdown-menu") {
                    li("dropdown") {
                        for ((name, language) in ctx.languages) {
                            if (name != locale) {
                                a(ctx.urlForLocale(name)) {
                                    tabIndex = "-1"
                                    div("f16") {
                                        i("flag ${language.flag}")
                                        +"- ${language.name}"
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

        if (!ctx.currentUser.anonymous) {
            li("dropdown") {
                a("#", classes = "dropdown-toggle") {
                    attributes["data-toggle"] = "dropdown"
                    span("fa fa-user")
                    +ctx.i18n(ctx.user.fullName)
                    b("caret")
                }
                ul("dropdown-menu") {
                    li {
                        a(ctx.userInfoUrl) {
                            span("fa fa-fw fa-user")
                            +ctx.i18n("Profile")
                        }
                    }
                    li {
                        a(ctx.logoutUrl) {
                            span("fa fa-fw fa-sign-out")
                            +ctx.i18n("Logout")
                        }
                    }
                }
            }
        } else {
            li {
                a(ctx.loginUrl) {
                    i("fa fa-fw fa-sign-in")
                    +ctx.i18n("Login")
                }
            }
        }
    }
}