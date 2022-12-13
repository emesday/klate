package emesday.klate.security.defaults

import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.*

object Permissions : IntIdTable("ab_permission") {

    val name = varchar("name", 100).uniqueIndex()
}

object ViewMenus : IntIdTable("ab_view_menu") {

    val name = varchar("name", 250).uniqueIndex()
}

object PermissionViewRoles : IntIdTable("ab_permission_view_role") {
    val permissionView = reference("permission_view", PermissionViews)
    val role = reference("role", Roles)

    init {
        uniqueIndex(permissionView, role)
    }
}

object Roles : IntIdTable("ab_role") {

    val name = varchar("name", 60).uniqueIndex()
}

object PermissionViews : IntIdTable("ab_permission_view") {

    val permission = reference("permission", Permissions)
    val viewMenu = reference("view_menu", ViewMenus)
}

object UserRoles : IntIdTable("ab_user_role") {
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val role = reference("role", Roles, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(user, role)
    }
}

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

object RegisterUser : IntIdTable("ab_register_user") {
    val firstName = varchar("first_name", 64)
    val lastName = varchar("last_name", 64)
    val username = varchar("username", 64).uniqueIndex()
    val password = varchar("password", 256).nullable()
    val email = varchar("email", 64).uniqueIndex()
    val registrationDate = datetime("registration_date").defaultExpression(CurrentDateTime()).nullable()
    val registrationHash = varchar("registration_hash", 256)
}
