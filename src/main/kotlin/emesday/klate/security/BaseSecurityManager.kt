package emesday.klate.security

import com.password4j.*
import emesday.klate.*
import emesday.klate.config.*
import emesday.klate.security.defaults.*
import emesday.klate.security.views.*
import io.ktor.server.application.*
import java.time.*

abstract class BaseSecurityManager<
        USER : User<Role<PERMISSION_VIEW>>,
        ROLE : Role<PERMISSION_VIEW>,
        PERMISSION : Permission,
        VIEW_MENU : ViewMenu,
        PERMISSION_VIEW : PermissionView<PERMISSION, VIEW_MENU>,
        >(
    application: Application,
) : AbstractSecurityManager<USER>(application) {

    // fun create_login_manager() required??
    // fun create_jwt_manager() required??

    val builtinRoles: Map<String, List<ViewMenuApiAndPermissionPair>>

    fun createBuiltinRoles(): Map<String, List<ViewMenuApiAndPermissionPair>> {
        return klate.app.roles
    }

    /**
     * Construct a list of FAB role objects, from a list of keys.
     * NOTE:
     * - keys are things like: "LDAP group DNs" or "OAUTH group names"
     * - we use AUTH_ROLES_MAPPING to map from keys, to FAB role names

     * @param roleKeys0: the list of FAB role keys
     * @return: a list of RoleModelView
     */
    fun getRolesFromKeys(roleKeys0: List<String>): Set<ROLE> {
        val roles = mutableSetOf<ROLE>()
        val roleKeys = roleKeys0.toMutableSet()
        for ((roleKey, klateRoleNames) in authRolesMapping) {
            if (roleKey in roleKeys) {
                for (klateRoleName in klateRoleNames) {
                    val klateRole = findRole(klateRoleName)
                    if (klateRole != null) {
                        roles.add(klateRole)
                    } else {
                        log.warn(
                            "Can't find role specified in AUTH_ROLES_MAPPING: $klateRoleName"
                        )
                    }
                }
            }
        }
        return roles
    }

    // def auth_type_provider_name(self) -> Optional[str]:
    // def get_url_for_registeruser(self):
    // def get_user_datamodel(self):
    // def get_register_user_datamodel(self):
    // done!: def builtin_roles(self) -> Dict[str, Any]:

    val apiLoginAllowMultipleProviders: Boolean = klate.auth.api.loginAllowMultipleProviders

    val authType: AuthType = klate.auth.type

    val authUsernameCI: Boolean = klate.auth.usernameCI

    val authRoleAdmin: String = klate.auth.role.admin

    val authRolePublic: String = klate.auth.role.public

    val authLdapServer: String
        get() = klate.auth.ldap.server

    val authLdapUseTls: Boolean = klate.auth.ldap.useTls

    val authUserRegistration: Boolean = klate.auth.user.registration

    val authUserRegistrationRole: String = klate.auth.user.registrationRole

    val authUserRegistrationRoleJMESPath: String? = klate.auth.user.registrationRoleJMESPath

    val authRolesMapping: Map<String, List<String>> = klate.auth.roles.mapping

    val authRolesSyncAtLogin: Boolean = klate.auth.roles.syncAtLogin

    val authLdapSearch: String = klate.auth.ldap.search

    val authLdapSearchFilter: String = klate.auth.ldap.searchFilter

    val authLdapBindUser: String = klate.auth.ldap.bindUser

    val authLdapBindPassword: String = klate.auth.ldap.bindPassword

    val authLdapAppendDomain: String = klate.auth.ldap.appendDomain

    val authLdapUsernameFormat: String = klate.auth.ldap.usernameFormat

    val authLdapUidField: String = klate.auth.ldap.uidField

    val authLdapGroupField: String = klate.auth.ldap.groupField

    val authLdapFirstnameField: String = klate.auth.ldap.firstNameField

    val authLdapLastnameField: String = klate.auth.ldap.lastNameField

    val authLdapEmailField: String = klate.auth.ldap.emailField

    // val authLdapBindFirst
    //     get() = klate.auth.ldap.bindFirst

    val authLdapAllowSelfSigned: Boolean = klate.auth.ldap.allowSelfSigned

    val authLdapTlsDemand: Boolean = klate.auth.ldap.tlsDemand

    val authLdapTlsCACertDir: String = klate.auth.ldap.tlsCACertDir

    val authLdapTlsCACertFile: String = klate.auth.ldap.tlsCACertFile

    val authLdapTlsCertFile: String = klate.auth.ldap.tlsCertFile

    val authLdapTlsKeyFile: String = klate.auth.ldap.tlsKeyFile

    // val openid_providers
    //     get() = klate

    val oauthProviders: List<String> = klate.auth.oauth.providers

    fun updateUserAuthStat(user: USER, success: Boolean = true) {
        user.mutate {
            if (success) {
                loginCount += 1
                lastLogin = LocalDateTime.now()
                failLoginCount = 0
            } else {
                failLoginCount += 1
                lastLogin
            }
        }
        updateUser(user)
    }

    abstract fun <T> withUsers(block: Iterable<USER>.() -> T): T

    abstract fun authUserOAuth(userInfo: UserInfo): USER?

    /**
     *
     * Method for authenticating user, auth db style
     *
     * @param username The username or registered email address
     * @param password The password, will be tested against hashed password on db
     */
    fun authUserDB(username: String, password: String): USER? {
        if (username == "") {
            return null
        }
        val firstUser = getFirstUser()
        var user = findUser(username = username)
        if (user == null) {
            user = findUser(email = username)
        } else {
            // Balance failure and success
            findUser(email = username)
        }

        if (user == null || !user.active) {
            // Balance failure and success
            Password.check("password", "").withBcrypt()
            log.info(LOGMSG_WAR_SEC_LOGIN_FAILED.format(username))
            // Balance failure and success
            if (firstUser != null) {
                noopUserUpdate(firstUser)
            }
            return null
        } else if (Password.check(password, user.password ?: "").withBcrypt()) {
            updateUserAuthStat(user, true)
            return user
        } else {
            updateUserAuthStat(user, false)
            log.info(LOGMSG_WAR_SEC_LOGIN_FAILED.format(username))
            return null
        }
    }

    fun oauthCalculateUserRoles(userInfo: UserInfo): Iterable<ROLE> {
        val userRoleObjects = mutableSetOf<ROLE>()

        // apply AUTH_ROLES_MAPPING
        if (authRolesMapping.isNotEmpty()) {
            val userRoleKeys = userInfo.roleKeys ?: emptyList()
            userRoleObjects.addAll(getRolesFromKeys(userRoleKeys))
        }

        // apply AUTH_USER_REGISTRATION_ROLE
        if (authUserRegistration) {
            val registrationRoleName = authUserRegistrationRole

            // TODO
            // if AUTH_USER_REGISTRATION_ROLE_JMESPATH is set,
            // use it for the registration role
            // if self.auth_user_registration_role_jmespath:
            //     import jmespath

            //     registration_role_name = jmespath.search(
            //     self.auth_user_registration_role_jmespath, userinfo

            val klateRole = findRole(registrationRoleName)
            if (klateRole != null) {
                userRoleObjects.add(klateRole)
            } else {
                log.warn(
                    "Can't find AUTH_USER_REGISTRATION role: $registrationRoleName"
                )
            }
        }
        return userRoleObjects
    }

    // ---------------------
    // PRIMITIVES FOR USERS
    // ---------------------

    /**
     * Generic function to return user registration
     */
    abstract fun findRegisterUser(registrationHash: String)

    /**
     * Generic function to add user registration
     */
    abstract fun addRegisterUser(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String = "",
        hashedPassword: String = "",
    )

    /**
     * Generic function to delete user registration
     */
    abstract fun delRegisterUser(registerUser: String)

    /**
     * Generic function to return user by it's id (pk)
     */
    abstract fun getUserById(pk: String)

    /**
     * Generic function find a user by it's username or email
     */
    abstract fun findUser(username: String? = null, email: String? = null): USER?

    /**
     * Generic function that returns all existing users
     */
    abstract fun getAllUsers(): List<USER>

    /**
     * Get all DB permissions from a role id
     */
    abstract fun getDBRolePermissions(roleId: Int)

    /**
     * Generic function to create user
     */
    abstract fun addUser(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        roles: Iterable<ROLE>,
        password: String = "",
    ): USER?

    /**
     * Generic function to update user
     * @param user User model to update to database
     */
    abstract fun updateUser(user: USER)

    /**
     * Generic function to count the existing users
     */
    abstract fun countUsers(): Int

    // ----------------------
    // PRIMITIVES FOR ROLES
    // ----------------------

    abstract fun findRole(role: String): ROLE?

    abstract fun addRole(role: String, permissions: List<PERMISSION_VIEW>? = null): ROLE?

    abstract fun updateRole(pk: Int, name: String)

    abstract fun getAllRoles(): List<ROLE>


    // ----------------------------
    // PRIMITIVES FOR PERMISSIONS
    // ----------------------------

    /**
     *  returns all permissions from public role
     */
    abstract fun getPublicRole()

    /**
     *  returns all permissions from public role
     */
    abstract fun getPublicPermission()

    /**
     *  Finds and returns a Permission by name
     */
    abstract fun findPermission()

    abstract fun findRolesPermissionViewMenus(permissionName: String, roleIds: List<Int>)

    /**
     * Finds and returns permission views for a group of roles
     */
    abstract fun existPermissionOnRoles(viewName: String, permissionName: String, roleIds: List<Int>)

    /**
     * Adds a permission to the backend, model permission
     * @param name name of the permission: 'can_add','can_edit' etc...
     */
    abstract fun addPermission(name: String)

    /**
     * Deletes a permission from the backend, model permission
     * @param name name of the permission: 'can_add','can_edit' etc...
     */
    abstract fun delPermission(name: String)


    // ----------------------
    // PRIMITIVES VIEW MENU
    // ----------------------

    /**
     *  Finds and returns a ViewMenu by name
     */
    abstract fun findViewMenu(name: String)

    abstract fun getAllViewMenu()

    /**
     * Adds a view or menu to the backend, model view_menu
     * @param name name of the view menu to add
     */
    abstract fun addViewMenu(name: String)

    /**
     * Deletes a ViewMenu from the backend
     * @param name name of the ViewMenu
     */
    abstract fun delViewMenu(name: String)


    // ----------------------
    // PERMISSION VIEW MENU
    // ----------------------

    /**
     * Finds and returns a PermissionView by names
     */
    abstract fun findPermissionViewMenu(permissionName: String, viewMenuName: String)

    /**
     * Finds all permissions from ViewMenu, returns list of PermissionView
     * @param viewMenu: ViewMenu object
     * @return: list of PermissionView objects
     */
    abstract fun findPermissionsViewMenu(viewMenu: String)

    /**
     * Adds a permission on a view or menu to the backend
     * @param permissionName name of the permission to add: 'can_add','can_edit' etc...
     * @param viewMenuName name of the view menu to add
     */
    abstract fun addPermissionViewMenu(permissionName: String, viewMenuName: String): PERMISSION_VIEW

    abstract fun delPermissionViewMenu(permissionName: String, viewMenuName: String, cascade: Boolean = true)

    abstract fun existPermissionOnViews(lst: String, item: String)

    abstract fun existPermissionOnView(lst: String, permission: String, viewMenu: String)

    /**
     * Add permission-ViewMenu object to Role
     * @param role The role object
     * @param permView The PermissionViewMenu object
     */
    abstract fun addPermissionRole(role: String, permView: String)

    /**
     * Remove permission-ViewMenu object to Role
     * @param role The role object
     * @param perm_view The PermissionViewMenu object
     */
    abstract fun delPermissionRole(role: String, permView: String)

    /**
     * Exports roles to JSON file.
     */
    abstract fun exportRoles(path: String, indent: String)

    /**
     * Imports roles from JSON file.
     */
    abstract fun importRoles(path: String)

    open fun <T> loadUser(pk: T) {
        // getUserById(pk)
    }

    open fun loadUserJwt(jwtHeader: String, jwtData: String) {
        // identity = jwt_data["sub"]
        // user = self.load_user(identity)
        // # Set flask g.user to JWT user, we can't do it on before request
        // g.user = user
        // return user
    }

    /**
     * Setups the DB, creates admin and public roles if they don't exist.
     */
    open fun createDB() {
        val rolesMapping = klate.app.rolesMapping
        for ((pk, name) in rolesMapping) {
            updateRole(pk, name)
        }
        for ((roleName, permissionViewMenus) in builtinRoles) {
            val permissions = permissionViewMenus.map { (viewMenuName, permissionName) ->
                addPermissionViewMenu(permissionName, viewMenuName)
            }
            addRole(roleName, permissions)
        }
        if (authRoleAdmin !in builtinRoles) {
            addRole(authRoleAdmin)
        }
        addRole(authRolePublic)
        if (countUsers() == 0) {
            log.warn(LOGMSG_WAR_SEC_NO_USER)
        }
    }

    init {
        builtinRoles = createBuiltinRoles()
    }

    val authView: AuthView = when (authType) {
        AuthType.OID -> AuthDBView()
        AuthType.DB -> AuthDBView()
        AuthType.LDAP -> AuthDBView()
        AuthType.REMOTE_USER -> AuthDBView()
        AuthType.OAUTH -> AuthDBView()
    }

    val urlForRegisterUser = ""
}
