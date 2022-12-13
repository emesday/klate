package emesday.klate.security

import java.time.*

interface Permission {
    var name: String
}

interface ViewMenu {
    var name: String
}

interface PermissionView <out PERMISSION : Permission, out VIEW_MENU : ViewMenu> {
    val permission: PERMISSION
    val viewMenu: VIEW_MENU
}

interface Role <out PERMISSION_VIEW: PermissionView<Permission, ViewMenu>> {
    var name: String
    val permissions: Iterable<PERMISSION_VIEW>

    fun <T> mutate(block: Role<PERMISSION_VIEW>.() -> T) {
        block(this)
    }
}

interface User <out ROLE : Role<PermissionView<Permission, ViewMenu>>> {

    var firstName: String

    var lastName: String

    var username: String

    var email: String

    var loginCount: Int

    var failLoginCount: Int

    var lastLogin: LocalDateTime?

    var active: Boolean

    val roles: Iterable<ROLE>

    fun <T> mutate(block: User<ROLE>.() -> T) {
        block(this)
    }
}

data class UserInfo(
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val roleKeys: List<String>? = null,
)