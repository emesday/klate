package emesday.klate.security.defaults

import emesday.klate.security.*
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

class PermissionEntity(id: EntityID<Int>) : IntEntity(id), Permission {
    companion object : IntEntityClass<PermissionEntity>(Permissions)

    override var name by Permissions.name
}

class ViewMenuEntity(id: EntityID<Int>) : IntEntity(id), ViewMenu {
    companion object : IntEntityClass<ViewMenuEntity>(ViewMenus)

    override var name by ViewMenus.name
}

class RoleEntity(id: EntityID<Int>) : IntEntity(id), Role<PermissionViewEntity> {
    companion object : IntEntityClass<RoleEntity>(Roles)

    override var name by Roles.name
    override var permissions by PermissionViewEntity via PermissionViewRoles

    override fun toString(): String = transaction {
        "Role(name='$name')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoleEntity

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

class PermissionViewEntity(id: EntityID<Int>) : IntEntity(id), PermissionView<PermissionEntity, ViewMenuEntity> {
    companion object : IntEntityClass<PermissionViewEntity>(PermissionViews)

    override var permission by PermissionEntity referencedOn PermissionViews.permission
    override var viewMenu by ViewMenuEntity referencedOn PermissionViews.viewMenu
}

class UserEntity(id: EntityID<Int>) : IntEntity(id), User<RoleEntity> {
    companion object : IntEntityClass<UserEntity>(Users)

    override var firstName by Users.firstName
    override var lastName by Users.lastName
    override var username by Users.username
    var password by Users.password
    override var active by Users.active

    override var email by Users.email
    override var lastLogin by Users.lastLogin
    override var loginCount by Users.loginCount
    override var failLoginCount by Users.failLoginCount

    override var roles: SizedIterable<RoleEntity> by RoleEntity via UserRoles

    var createdOn by Users.createdOn
    var changedOn by Users.changedOn
    var createdBy by Users.createdBy
    var changedBy by Users.changedBy
    override fun <T> mutate(block: User<RoleEntity>.() -> T) = transaction {
        super.mutate(block)
    }

    override fun toString(): String = transaction {
        "User(username='$username', active=$active, email='$email', roles=${roles})"
    }
}