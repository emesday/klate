package emesday.klate.form

import io.ktor.server.application.*
import kotlinx.html.*

interface Field {

    val name: String

    fun render(
        content: FlowOrInteractiveOrPhrasingContent,
        block: INPUT.() -> Unit,
    )
}

class StringField(
    override val name: String,
    val label: String,
    val validators: List<Any>,
) : Field {

    override fun render(
        content: FlowOrInteractiveOrPhrasingContent,
        block: INPUT.() -> Unit,
    ) {
        content.textInput {
            name = this@StringField.name
            block()
        }
    }
}

class PasswordField(
    override val name: String,
    val label: String,
    val validators: List<Any>,
) : Field {
    override fun render(
        content: FlowOrInteractiveOrPhrasingContent,
        block: INPUT.() -> Unit,
    ) {
        content.passwordInput {
            name = this@PasswordField.name
            block()
        }
    }
}


open class Form {

    fun string(name: String, label: String, validators: List<Any> = emptyList()) = StringField(name, label, validators)
    fun password(name: String, label: String, validators: List<Any> = emptyList()) =
        PasswordField(name, label, validators)

    val errors: Map<String, List<String>> = emptyMap()

    fun validate(call: ApplicationCall) {}
}

object LoginForm : Form() {

    val username = string("username", "User name")

    val password = password("password", "Password")
}
