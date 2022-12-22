package emesday.klate.templates.ab.general.security

import emesday.klate.form.*
import emesday.klate.templates.*
import emesday.klate.templates.ab.*
import kotlinx.html.*

class LoginDB(val form: LoginForm) : Base() {

    var title: String = ""

    override fun HTML.apply(ctx: KlateContext) {
        this@LoginDB.content {
            div("container") {
                div("mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2") {
                    id = "loginbox"
                    style = "margin-top: 50px;"
                    div("panel panel-primary") {
                        div("panel-heading") {
                            div("panel-title") {
                                +this@LoginDB.title
                            }
                        }
                        div("panel-body") {
                            style = "padding-top: 30px;"
                            form(action = "", classes = "form", method = FormMethod.post) {
                                name = "login"
                                form.hiddenTag(this)
                                div("help-block") {
                                    ctx.i18n("Enter your login and password below")
                                }
                                val additionalClass = if ("openid" in form.errors) {
                                    "error"
                                } else {
                                    ""
                                }
                                div("control-group $additionalClass") {
                                    label("control-label") {
                                        htmlFor = "username"
                                        +ctx.i18n("Username")
                                    }
                                    div("controls") {
                                        div("input-group") {
                                            span("input-group-addon") {
                                                i("fa fa-user")
                                            }
                                            render(form.username) {
                                                size = "80"
                                                classes = setOf("form-control")
                                                autoFocus = true
                                            }
                                        }
                                        for (error in form.errors.getOrDefault("openid", emptyList())) {
                                            span("help-inline") {
                                                +error
                                            }
                                            br()
                                        }
                                        label("control-label") {
                                            htmlFor = "password"
                                            +ctx.i18n("Password")
                                        }
                                        div("input-group") {
                                            span("input-group-addon") {
                                                i("fa fa-key")
                                            }
                                            render(form.password) {
                                                size = "80"
                                                classes = setOf("form-control")
                                            }
                                        }
                                        for (error in form.errors.getOrDefault("openid", emptyList())) {
                                            span("help-inline") {
                                                +error
                                            }
                                            br()
                                        }
                                    }
                                }
                                div("control-group") {
                                    div("controls") {
                                        br()
                                        div {
                                            input {
                                                classes = setOf("btn", "btn-primary", "btn-block")
                                                type = InputType.submit
                                                value = ctx.i18n("Sign In")
                                            }
                                            if (ctx.klate?.securityManager?.authUserRegistration == true) {
                                                a(ctx.registerUserUrl) {
                                                    classes = setOf("btn btn-block btn-primary")
                                                    attributes["data-toggle"] = "tooltip"
                                                    rel = "tooltip"
                                                    title = ctx.i18n("If you are not already a user, please register")
                                                    +ctx.i18n("Register")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}