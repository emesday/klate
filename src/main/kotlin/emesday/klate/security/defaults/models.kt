package emesday.klate.security.defaults

import emesday.klate.security.*
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.*
import org.jetbrains.exposed.sql.transactions.*

// --

object Permissions : IntIdTable("ab_permission") {

    val name = varchar("name", 100).uniqueIndex()
}

class Permission(id: EntityID<Int>) : IntEntity(id), PermissionItf {
    companion object : IntEntityClass<Permission>(Permissions)

    override var name by Permissions.name
}

object ViewMenus : IntIdTable("ab_view_menu") {

    val name = varchar("name", 250).uniqueIndex()
}

class ViewMenu(id: EntityID<Int>) : IntEntity(id), ViewMenuItf {
    companion object : IntEntityClass<ViewMenu>(ViewMenus)

    override var name by ViewMenus.name
}

// --

object PermissionViewRoles : IntIdTable("ab_permission_view_role") {
    val permissionView = reference("permission_view", PermissionViews)
    val role = reference("role", Roles)

    init {
        uniqueIndex(permissionView, role)
    }
}

// --

object Roles : IntIdTable("ab_role") {

    val name = varchar("name", 60).uniqueIndex()
}

class Role(id: EntityID<Int>) : IntEntity(id), RoleItf {
    companion object : IntEntityClass<Role>(Roles)

    override var name by Roles.name
    var permissions by PermissionView via PermissionViewRoles

    override fun toString(): String = transaction {
        "Role(name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

// --

object PermissionViews : IntIdTable("ab_permission_view") {

    val permission = reference("permission", Permissions)
    val viewMenu = reference("view_menu", ViewMenus)
}

class PermissionView(id: EntityID<Int>) : IntEntity(id), PermissionViewItf<Permission, ViewMenu> {
    companion object : IntEntityClass<PermissionView>(PermissionViews)

    override var permission by Permission referencedOn PermissionViews.permission
    override var viewMenu by ViewMenu referencedOn PermissionViews.viewMenu
}

object UserRoles : IntIdTable("ab_user_role") {
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val role = reference("role", Roles, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(user, role)
    }
}

// --

object Users : IntIdTable("ab_user") {

    val firstName = varchar("first_name", 64)
    val lastName = varchar("last_name", 64)
    val username = varchar("username", 64).uniqueIndex()
    val password = varchar("password", 256).nullable()
    val active = bool("active")
    val email = varchar("email", 64).uniqueIndex()
    val lastLogin = datetime("last_login").nullable()
    val loginCount = integer("login_count")
    val failLoginCount = integer("fail_login_count")
    val createdOn = datetime("created_on").defaultExpression(CurrentDateTime())
    val changedOn = datetime("changed_on").defaultExpression(CurrentDateTime())
    val createdBy = reference("created_by", Users).nullable()
    val changedBy = reference("changed_by", Users).nullable()
}

class User(id: EntityID<Int>) : IntEntity(id), UserItf<Role> {
    companion object : IntEntityClass<User>(Users)

    override var firstName by Users.firstName
    override var lastName by Users.lastName
    override var username by Users.username
    var password by Users.password
    override var active by Users.active

    override var email by Users.email
    override var lastLogin by Users.lastLogin
    override var loginCount by Users.loginCount
    override var failLoginCount by Users.failLoginCount

    override var roles: SizedIterable<Role> by Role via UserRoles

    var createdOn by Users.createdOn
    var changedOn by Users.changedOn
    var createdBy by Users.createdBy
    var changedBy by Users.changedBy
    override fun <T> mutate(block: UserItf<Role>.() -> T) = transaction {
        super.mutate(block)
    }

    override fun toString(): String = transaction {
        "User(username='$username', active=$active, email='$email', roles=${roles})"
    }
}

object RegisterUser : IntIdTable("ab_register_user") {
    val firstName = varchar("first_name", 64)
    val lastName = varchar("last_name", 64)
    val username = varchar("username", 64).uniqueIndex()
    val password = varchar("password", 256).nullable()
    val email = varchar("email", 64).uniqueIndex()
    val registrationDate = datetime("registration_date").defaultExpression(CurrentDateTime()).nullable()
    val registrationHash = varchar("registration_hash", 256)
}
