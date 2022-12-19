package emesday.klate.form

import io.ktor.server.html.*
import kotlinx.html.*
import kotlinx.html.attributes.*
import kotlinx.html.dom.*
import kotlinx.html.stream.*
import org.jetbrains.exposed.exceptions.*
import org.jetbrains.exposed.sql.*

open class Widget

abstract class Input(
    val inputType: String,
    val validationAttrs: List<String> = listOf("required")
): Widget() {

    open operator fun invoke() {
    }
}

/**
 * Render a single-line text input.
 */
class TextInput : Input(
    "text",
    listOf("required", "maxlength", "minlength", "pattern")
)

abstract class Field<T> {

    abstract val widget: Widget

    var data: T? = null

    open fun processFormData(valueList: List<T>) {
        if (valueList.isNotEmpty()) {
            data = valueList.first()
        }
    }
}

open class StringField : Field<String>() {

    override val widget: Widget = TextInput()

    protected open val value: String
        get() = data ?: ""
}

open class Form {

    fun hiddenTag(form: FORM): Unit = with(form) {
    }

//    private val _columns = mutableListOf<Column<*>>()

//    private fun MutableList<Column<*>>.addColumn(column: Column<*>) {
//        if (this.any { it.name == column.name }) {
//            throw DuplicateColumnException(column.name, tableName)
//        }
//        this.add(column)
//    }

//    fun <T> registerField(name: String, type: IColumnType): Column<T> = Column<T>(this, name, type).also { _columns.addColumn(it) }

    fun string(name: String, label: String, validators: List<Any> = emptyList()): Placeholder<INPUT> = Placeholder()
    fun password(name: String, label: String, validators: List<Any> = emptyList()): Placeholder<INPUT> = Placeholder()

    val errors: Map<String, List<String>> = emptyMap()
}

class LoginForm : Form() {
    fun FlowOrInteractiveOrPhrasingContent.username(block: INPUT.() -> Unit): Unit = input {
        type = InputType.text
        block()
    }

    fun FlowOrInteractiveOrPhrasingContent.password(block: INPUT.() -> Unit): Unit = input {
        type = InputType.password
        block()
    }
}
