package emesday.klate.deprecated.appbuilder

fun String.translate(): String = this

fun urlFor(url: String): String {
    return url
}

//val currentAppBuilder: AppBuilder = throw NotImplementedError()

data class MenuItemToShow(
    val name: String,
    val icon: String? = null,
    val label: String? = null,
    val url: String? = null,
    val children: List<MenuItemToShow>? = null,
)

val separatorToShow = MenuItemToShow("-", "-", "-")

data class MenuItem(
    val name: String,
    val href: String? = null,
    val icon: String? = null,
    val label: String? = null,
    val children: MutableList<MenuItem> = mutableListOf(),
//    val baseView: emesday.klate.deprecated.appbuilder.AbstractViewApi? = null,
    val cond: (() -> Boolean)? = null,
) {

    fun shouldRender(): Boolean {
        val notNullCond = cond ?: return true
        return notNullCond()
    }

    fun getUrl(): String {
//        return if (href == null) {
//            if (baseView == null) {
//                ""
//            } else {
//                urlFor("${baseView.endpoint}.${baseView.defaultView}")
//            }
//        } else {
//            try {
//                urlFor(href)
//            } catch (_: Exception) {
//                href
//            }
//        }
        return ""
    }

    override fun toString(): String {
        return name
    }
}

class Menu(
    reverse: Boolean = true,
    extraClasses: String = "",
) {

    val menu: MutableList<MenuItem> = mutableListOf()
    val extraClasses = if (reverse) "$extraClasses navbar-inverse".trim() else extraClasses

    val reverse: Boolean
        get() = "navbar-inverse" in extraClasses

    fun getList(): List<MenuItem> = menu

    // TODO: strange logic....
    fun getFlatNameList(
        menu: List<MenuItem> = this.menu,
        result: MutableList<String> = mutableListOf(),
    ): MutableList<String> {
        for (item in menu) {
            result.add(item.name)
            if (item.children.isNotEmpty()) {
                result.addAll(getFlatNameList(item.children, result))
            }
        }
        return result
    }

    fun getData(menu: List<MenuItem> = this.menu): List<MenuItemToShow> {
        val retList = mutableListOf<MenuItemToShow>()
//        val allowedMenus = currentAppBuilder.sm.getUserMenuAccess(
//            getFlatNameList()
//        )
        for ((i, item) in menu.withIndex()) {
            if (!item.shouldRender()) {
                continue
            }
            if (item.name == "-" && i != menu.size - 1) {
                retList.add(separatorToShow)
//            } else if (item.name !in allowedMenus) {
//                continue
            } else if (item.children.isNotEmpty()) {
                retList.add(
                    MenuItemToShow(
                        name = item.name,
                        icon = item.icon,
                        label = item.label?.translate(),
                        children = getData(item.children)
                    )
                )

            } else {
                retList.add(
                    MenuItemToShow(
                        name = item.name,
                        icon = item.icon,
                        label = item.label?.translate(),
                        url = item.getUrl()
                    )
                )
            }
        }
        return retList
    }

    fun find(name: String, menu: List<MenuItem>? = null): MenuItem? {
        val m = menu ?: this.menu
        for (i in m) {
            if (i.name == name) {
                return i
            } else {
                if (i.children.isNotEmpty()) {
                    val retItem = find(name, i.children)
                    if (retItem != null) {
                        return retItem
                    }
                }
            }
        }
        return null
    }

    fun addCategory(
        category: String,
        icon: String? = null,
        label: String? = null,
        parentCategory: String? = null,
    ) {
        val l = label ?: category
        val newItem = MenuItem(name = category, icon = icon, label = l)
        if (parentCategory == null) {
            menu.add(newItem)
        } else {
            val parent = find(category) ?: throw Exception("no parent category")
            parent.children.add(newItem)
        }
    }

    fun addLink(
        name: String,
        href: String? = null,
        icon: String? = null,
        label: String? = null,
        category: String? = null,
        categoryIcon: String? = null,
        categoryLabel: String? = null,
//        baseView: emesday.klate.deprecated.appbuilder.AbstractViewApi? = null,
        cond: (() -> Boolean)? = null,
    ) {
        val newItem = MenuItem(
            name = name,
            href = href,
            icon = icon,
            label = label ?: name,
//            baseView = baseView,
            cond = cond
        )
        if (category == null) {
            menu.add(newItem)
        } else {
            val menuItem = find(category)
            if (menuItem != null) {
                menuItem.children.add(menuItem)
            } else {
                addCategory(
                    category, categoryIcon, categoryLabel ?: category
                )
                val parent = find(category) ?: throw Exception("no parent category")
                parent.children.add(newItem)
            }
        }
    }

    fun addSeparator(category: String, cond: (() -> Boolean)? = null) {
        val menuItem = find(category)
        if (menuItem != null) {
            menuItem.children.add(MenuItem("-", cond = cond))
        } else {
            throw Error(
                "Menu separator does not have correct category $category"
            )
        }
    }


}
