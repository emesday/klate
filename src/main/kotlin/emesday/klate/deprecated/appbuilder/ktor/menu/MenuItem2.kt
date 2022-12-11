package emesday.klate.deprecated.appbuilder.ktor.menu

data class MenuItem2(
    override val url: String,
    override val label: String,
    override val icon: String? = null,
    val cond: (() -> Boolean)? = null,
) : MenuItem {

    override fun isVisible(): Boolean {
        return cond?.let { it() } ?: super.isVisible()
    }
}