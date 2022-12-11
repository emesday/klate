package emesday.klate.deprecated.appbuilder.ktor.menu

interface MenuItem {

    val url: String

    val label: String

    val icon: String?

    fun getChildren(): List<MenuItem>? = null

    fun isVisible(): Boolean = true

    fun isSeperator(): Boolean = false
}