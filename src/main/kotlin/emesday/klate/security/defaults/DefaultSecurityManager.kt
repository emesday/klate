package emesday.klate.security.defaults

import emesday.klate.exceptions.*
import emesday.klate.security.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.*

/**
 * Responsible for authentication, registering security views,
 * role and permission auto management
 *
 * If you want to change anything just inherit and override, then
 * pass your own security manager to AppBuilder.
 */
open class DefaultSecurityManager(application: Application) :
    BaseSecurityManager(application) {

    init {
        createDefaultSecurityManagerTables()
    }

    override fun updateUser(user: UserItf) {
        // required ?
    }

    override fun countUsers(): Int {
        TODO("Not yet implemented")
    }

    override fun addUser(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        roles: List<RoleItf>,
        password: String,
    ): UserItf? = run {
        val user = transaction {
            User.new {
                this.firstName = firstName
                this.lastName = lastName
                this.username = username
                this.password = password
                this.active = true
                this.email = email
                this.lastLogin = null
                this.loginCount = 0
                this.failLoginCount = 0
            }
        }

        transaction {
            user.roles = roles
        }

        user
    }

    override fun <T> withUsers(block: Iterable<UserItf>.() -> T): T = transaction {
        User.all().block()
    }

    override fun authUserOAuth(userInfo: UserInfo): UserItf? {
        val username = userInfo.username
            ?: userInfo.email
            ?: throw KlateException("OAUTH userinfo does not have username or email")

        // If username is empty, go away
        if (username == "") {
            return null
        }

        var user = findUser(username = username)

        // If user is not active, go away
        if (user?.active == false) {
            return null
        }

        // If user is not registered, and not self-registration, go away
        if (user == null && !authUserRegistration) {
            return null
        }

        // Sync the user's roles
        if (user != null && authRolesSyncAtLogin) {
            val userLocal = user
            transaction {
                userLocal.roles = oauthCalculateUserRoles(userInfo)
            }
            transaction {
                log.debug(
                    "Calculated new roles for user='$username' as: ${userLocal.roles}"
                )
            }
        }

        // If the user is new, register them
        if (user == null && authUserRegistration) {
            user = addUser(
                username = username,
                firstName = userInfo.firstName ?: "",
                lastName = userInfo.lastName ?: "",
                email = userInfo.email ?: "$username@email.notfound",
                roles = oauthCalculateUserRoles(userInfo)
            )
            log.debug("New user registered: $user")

            // If user registration failed, go away
            if (user == null) {
                log.error("Error creating a new OAuth user $username")
                return null
            }
        }

        // LOGIN SUCCESS (only if user is now registered)
        return if (user != null) {
            updateUserAuthStat(user)
            user
        } else {
            null
        }
    }

    override fun findRegisterUser(registrationHash: String) {
        TODO("Not yet implemented")
    }

    override fun addRegisterUser(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        hashedPassword: String,
    ) {
        TODO("Not yet implemented")
    }

    override fun delRegisterUser(registerUser: String) {
        TODO("Not yet implemented")
    }

    override fun getUserById(pk: String) {
        TODO("Not yet implemented")
    }

    override fun addRole(role: String): RoleItf? = transaction {
        Role.new {
            name = role
        }
    }

    override fun updateRole(pk: String, name: String) {
        TODO("Not yet implemented")
    }

    override fun getAllRoles(): List<RoleItf> {
        TODO("Not yet implemented")
    }

    override fun getPublicRole() {
        TODO("Not yet implemented")
    }

    override fun getPublicPermission() {
        TODO("Not yet implemented")
    }

    override fun findPermission() {
        TODO("Not yet implemented")
    }

    override fun findRolesPermissionViewMenus(permissionName: String, roleIds: List<Int>) {
        TODO("Not yet implemented")
    }

    override fun existPermissionOnRoles(viewName: String, permissionName: String, roleIds: List<Int>) {
        TODO("Not yet implemented")
    }

    override fun addPermission(name: String) {
        TODO("Not yet implemented")
    }

    override fun delPermission(name: String) {
        TODO("Not yet implemented")
    }

    override fun findViewMenu(name: String) {
        TODO("Not yet implemented")
    }

    override fun getAllViewMenu() {
        TODO("Not yet implemented")
    }

    override fun addViewMenu(name: String) {
        TODO("Not yet implemented")
    }

    override fun delViewMenu(name: String) {
        TODO("Not yet implemented")
    }

    override fun findPermissionViewMenu(permissionName: String, viewMenuName: String) {
        TODO("Not yet implemented")
    }

    override fun findPermissionsViewMenu(viewMenu: String) {
        TODO("Not yet implemented")
    }

    override fun findRole(role: String): RoleItf? = transaction {
        Role.find(Roles.name eq role).firstOrNull()
    }

    override fun findUser(username: String?, email: String?): UserItf? = transaction {
        if (username != null) {
            if (authUsernameCI) {
                User.find(
                    Users.username.lowerCase() eq username.lowercase()
                ).firstOrNull()
            } else {
                User.find(
                    Users.username eq username
                ).firstOrNull()
            }
        } else if (email != null) {
            User.find(
                Users.email eq email
            ).firstOrNull()
        } else {
            null
        }
    }

    override fun getAllUsers(): List<UserItf> {
        TODO("Not yet implemented")
    }

    override fun getDBRolePermissions(roleId: Int) {
        TODO("Not yet implemented")
    }

    override fun addPermissionViewMenu(
        permissionName: String,
        viewMenuName: String,
    ) {


        //        if not (permission_name and view_menu_name):
        //            return None
        //        pv = self.find_permission_view_menu(permission_name, view_menu_name)
        //        if pv:
        //            return pv
        //        vm = self.add_view_menu(view_menu_name)
        //        perm = self.add_permission(permission_name)
        //        pv = self.permissionview_model()
        //        pv.view_menu, pv.permission = vm, perm
        //        try:
        //            self.get_session.add(pv)
        //            self.get_session.commit()
        //            log.info(c.LOGMSG_INF_SEC_ADD_PERMVIEW.format(str(pv)))
        //            return pv
        //        except Exception as e:
        //            log.error(c.LOGMSG_ERR_SEC_ADD_PERMVIEW.format(str(e)))
        //            self.get_session.rollback()

    }

    override fun delPermissionViewMenu(permissionName: String, viewMenuName: String, cascade: Boolean) {
        TODO("Not yet implemented")
    }

    override fun existPermissionOnViews(lst: String, item: String) {
        TODO("Not yet implemented")
    }

    override fun existPermissionOnView(lst: String, permission: String, viewMenu: String) {
        TODO("Not yet implemented")
    }

    override fun addPermissionRole(role: String, permView: String) {
        TODO("Not yet implemented")
    }

    override fun delPermissionRole(role: String, permView: String) {
        TODO("Not yet implemented")
    }

    override fun exportRoles(path: String, indent: String) {
        TODO("Not yet implemented")
    }

    override fun importRoles(path: String) {
        TODO("Not yet implemented")
    }

    override fun addPermissionView(basePermissions: String, viewMenu: String) {
        TODO("Not yet implemented")
    }

    override fun addPermissionMenu(viewMenuName: String) {
        TODO("Not yet implemented")
    }

    override fun registerViews() {
        TODO("Not yet implemented")
    }

    override fun isItemPublic(permissionName: String, viewName: String) {
        TODO("Not yet implemented")
    }

    override fun hasAccess(permissionName: String, viewMenu: String) {
        TODO("Not yet implemented")
    }

    override fun securityCleanup(baseviews: String, menus: String) {
        TODO("Not yet implemented")
    }

    override fun getFirstUser() {
        TODO("Not yet implemented")
    }

    override fun noopUserUpdate(user: String) {
        TODO("Not yet implemented")
    }
}

fun createDefaultSecurityManagerTables() {
    transaction {
        SchemaUtils.create(
            Permissions,
            ViewMenus,
            Roles,
            PermissionViews,
            PermissionViewRoles,
            UserRoles,
            Users,
            RegisterUser,
            inBatch = true
        )
    }
}
