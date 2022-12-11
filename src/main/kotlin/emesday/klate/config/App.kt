package emesday.klate.config

import io.ktor.server.config.*

data class ViewMenuApiAndPermissionPair(
    // VIEW/MENU/API NAME
    val viewMenuApiName: String,
    // PERMISSION NAME
    val permissionName: String,
)


class KlateApplicationConfigApp(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    var addSecurityViews: Boolean
        get() = ac.boolean(this.combine("addSecurityViews")) ?: true
        set(value) {
            ac.put(this.combine("addSecurityViews"), value)
        }

    var roles: Map<String, ViewMenuApiAndPermissionPair> = emptyMap()
//        get() = mapGetter("roles")
//        set(value) = mapSetter("roles", value)

    var rolesMapping: Map<Int, String> = emptyMap()
//        get() = mapGetter("rolesMapping")
//        set(value) = mapSetter("rolesMapping", value)

    var indexView: String?
        get() = ac.string(combine("indexView"))
        set(value) = ac.put(combine("indexView"), value)
}