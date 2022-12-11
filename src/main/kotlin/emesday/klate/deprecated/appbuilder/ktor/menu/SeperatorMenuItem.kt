package emesday.klate.deprecated.appbuilder.ktor.menu

val seperatorMenuItem = object : MenuItem {
    override val url: String = ""
    override val label: String = ""
    override val icon: String? = null
    override fun isSeperator(): Boolean = true
}