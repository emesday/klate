package emesday.klate.deprecated.appbuilder.ktor.menu

data class CategoryMenuItem(
    override val label: String,
    private val children: List<MenuItem>,
    override val icon: String? = null,
    val cond: (() -> Boolean)? = null,
) : MenuItem {
    override val url: String = ""

    override fun isVisible(): Boolean {
        return cond?.let { it() } ?: super.isVisible()
    }

    override fun getChildren(): List<MenuItem>? {
        return children
    }
}