package emesday.klate.security

import emesday.klate.manager.*
import io.ktor.server.application.*

/**
 * Abstract SecurityManager class, declares all methods used by the
 * framework. There is no assumptions about security models or auth types.
 */
abstract class AbstractSecurityManager<
        USER : User<*>,
        >(
    application: Application,
) : BaseManager(application) {

    /**
     * Adds a permission on a view menu to the backend
     * @param basePermissions list of permissions from view (all exposed methods):
     *                         'can_add','can_edit' etc...
     * @param viewMenu name of the view or menu to add
     */
    abstract fun addPermissionView(basePermissions: String, viewMenu: String)

    /**
     * Adds menu_access to menu on permission_view_menu
     * @param view_menu_name The menu name
     */
    abstract fun addPermissionMenu(viewMenuName: String)

    /**
     * Generic function to create the security views
     */
    abstract fun registerViews()

    /**
     * Check if view has public permissions
     * @param permissionName the permission: can_show, can_edit...
     * @param viewName the name of the class view (child of BaseView)
     */
    abstract fun isItemPublic(permissionName: String, viewName: String)

    /**
     * Check if current user or public has access to view or menu
     */
    abstract fun hasAccess(permissionName: String, viewMenu: String)

    abstract fun securityCleanup(baseviews: String, menus: String)

    abstract fun getFirstUser(): USER?

    abstract fun noopUserUpdate(user: USER)


}