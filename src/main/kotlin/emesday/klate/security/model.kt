package emesday.klate.security

import java.time.*

interface PermissionItf {
    var name: String
}

interface ViewMenuItf {
    var name: String
}

interface PermissionViewItf <out PERMISSION : PermissionItf, out VIEW_MENU : ViewMenuItf> {
    val permission: PERMISSION
    val viewMenu: VIEW_MENU
}

interface RoleItf {
    var name: String

    fun <T> mutate(block: RoleItf.() -> T) {
        block(this)
    }
}

interface UserItf <out ROLE : RoleItf> {

    var firstName: String

    var lastName: String

    var username: String

    var email: String

    var loginCount: Int

    var failLoginCount: Int

    var lastLogin: LocalDateTime?

    var active: Boolean

    val roles: Iterable<ROLE>

    fun <T> mutate(block: UserItf<ROLE>.() -> T) {
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