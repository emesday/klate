package emesday.klate.form

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

    fun hiddenTag(): String = ""

    private val _columns = mutableListOf<Column<*>>()

//    private fun MutableList<Column<*>>.addColumn(column: Column<*>) {
//        if (this.any { it.name == column.name }) {
//            throw DuplicateColumnException(column.name, tableName)
//        }
//        this.add(column)
//    }

//    fun <T> registerField(name: String, type: IColumnType): Column<T> = Column<T>(this, name, type).also { _columns.addColumn(it) }

//    fun string(name: String, label: String, validators: List<Any> = emptyList()): Field<String> = registerField(name, VarCharColumnType(length, collate))
}

object LoginForm1 : Form() {
//    val username = string("username", "User Name")
}