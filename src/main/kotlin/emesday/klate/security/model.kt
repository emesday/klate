package emesday.klate.security

import java.time.*

interface RoleItf {
    var name: String

    fun <T> mutate(block: RoleItf.() -> T) {
        block(this)
    }
}

interface UserItf {

    var firstName: String

    var lastName: String

    var username: String

    var email: String

    var loginCount: Int

    var failLoginCount: Int

    var lastLogin: LocalDateTime?

    var active: Boolean

    var roles: List<RoleItf>

    fun <T> mutate(block: UserItf.() -> T) {
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