package emesday.klate

const val LOGMSG_ERR_SEC_ACCESS_DENIED = "Access is Denied for: %s on: %s" +
        " Access denied log message, format with user and view/resource "
const val LOGMSG_WAR_SEC_LOGIN_FAILED = "Login Failed for user: %s"
const val LOGMSG_ERR_SEC_CREATE_DB = "DB Creation and initialization failed: %s" +
        " security models creation fails, format with error message "
const val LOGMSG_ERR_SEC_ADD_ROLE = "Add Role: %s" +
        " Error adding role, format with err message "
const val LOGMSG_ERR_SEC_ADD_PERMISSION = "Add Permission: %s" +
        " Error adding permission, format with err message "
const val LOGMSG_ERR_SEC_ADD_VIEWMENU = "Add View Menu Error: %s" +
        " Error adding view menu, format with err message "
const val LOGMSG_ERR_SEC_DEL_PERMISSION = "Del Permission Error: %s" +
        " Error deleting permission, format with err message "
const val LOGMSG_ERR_SEC_ADD_PERMVIEW = "Creation of Permission View Error: %s" +
        " Error adding permission view, format with err message "
const val LOGMSG_ERR_SEC_DEL_PERMVIEW = "Remove Permission from View Error: %s" +
        " Error deleting permission view, format with err message "
const val LOGMSG_WAR_SEC_DEL_PERMVIEW = "Refused to delete permission view, assoc with role exists %s.%s %s"

const val LOGMSG_WAR_SEC_DEL_PERMISSION = "Refused to delete, permission %s does not exist"
const val LOGMSG_WAR_SEC_DEL_VIEWMENU = "Refused to delete, view menu %s does not exist"
const val LOGMSG_WAR_SEC_DEL_PERM_PVM = "Refused to delete permission %s, PVM exists %s"
const val LOGMSG_WAR_SEC_DEL_VIEWMENU_PVM = "Refused to delete view menu %s, PVM exists %s"
const val LOGMSG_ERR_SEC_ADD_PERMROLE = "Add Permission to Role Error: %s" +
        " Error adding permission to role, format with err message "
const val LOGMSG_ERR_SEC_DEL_PERMROLE = "Remove Permission to Role Error: %s" +
        " Error deleting permission to role, format with err message "
const val LOGMSG_ERR_SEC_ADD_REGISTER_USER = "Add Register User Error: %s" +
        " Error adding registered user, format with err message "
const val LOGMSG_ERR_SEC_DEL_REGISTER_USER = "Remove Register User Error: %s" +
        " Error deleting registered user, format with err message "
const val LOGMSG_ERR_SEC_NO_REGISTER_HASH = "Attempt to activate user with false hash: %s" +
        " Attempt to activate user with not registered hash, format with hash "
const val LOGMSG_ERR_SEC_AUTH_LDAP = "LDAP Error %s" +
        " Generic LDAP error, format with err message "
const val LOGMSG_ERR_SEC_AUTH_LDAP_TLS = "LDAP Could not activate TLS on established connection with %s" +
        " LDAP Could not activate TLS on established connection with server "
const val LOGMSG_ERR_SEC_ADD_USER = "Error adding new user to database. %s" +
        " Error adding user, format with err message "
const val LOGMSG_ERR_SEC_UPD_USER = "Error updating user to database. %s " +
        " Error updating user, format with err message "
const val LOGMSG_WAR_SEC_NO_USER = "No user yet created, use flask fab command to do it." +
        " Warning when app starts if no user exists on db "
const val LOGMSG_WAR_SEC_NOLDAP_OBJ = "No LDAP object found for: %s"

const val LOGMSG_INF_SEC_ADD_PERMVIEW = "Created Permission View: %s" +
        " Info when adding permission view, format with permission view class string "
const val LOGMSG_INF_SEC_DEL_PERMVIEW = "Removed Permission View: %s on %s" +
        " Info when deleting permission view, format with permission name and view name "
const val LOGMSG_INF_SEC_ADD_PERMROLE = "Added Permission %s to role %s" +
        " Info when adding permission to role, format with permission view class string and role name "
const val LOGMSG_INF_SEC_DEL_PERMROLE = "Removed Permission %s to role %s" +
        " Info when deleting permission to role, format with permission view class string and role name "
const val LOGMSG_INF_SEC_ADD_ROLE = "Inserted Role: %s" +
        " Info when added role, format with role name "
const val LOGMSG_INF_SEC_NO_DB = "Security DB not found Creating all Models from Base"
const val LOGMSG_INF_SEC_ADD_DB = "Security DB Created"
const val LOGMSG_INF_SEC_ADD_USER = "Added user %s" +
        " User added, format with username "
const val LOGMSG_INF_SEC_UPD_USER = "Updated user %s" +
        " User updated, format with username "
const val LOGMSG_INF_SEC_UPD_ROLE = "Updated role %s" +
        " Role updated, format with role name "
const val LOGMSG_ERR_SEC_UPD_ROLE = "An error occurred updating role %s" +
        " Role updated Error, format with role name "

const val LOGMSG_INF_FAB_ADDON_ADDED = "Registered AddOn: %s" +
        " Addon imported and registered "
const val LOGMSG_ERR_FAB_ADDON_IMPORT = "An error occurred when importing declared addon %s: %s" +
        " Error on addon import, format with addon class path and error message "
const val LOGMSG_ERR_FAB_ADDON_PROCESS = "An error occurred when processing declared addon %s: %s" +
        " Error on addon processing (pre, register, post), format with addon class path and error message "

const val LOGMSG_ERR_FAB_ADD_PERMISSION_MENU = "Add Permission on Menu Error: %s" +
        " Error when adding a permission to a menu, format with err "
const val LOGMSG_ERR_FAB_ADD_PERMISSION_VIEW = "Add Permission on View Error: %s" +
        " Error when adding a permission to a menu, format with err "

const val LOGMSG_ERR_DBI_ADD_GENERIC = "Add record error: %s" +
        " Database add generic error, format with err message "
const val LOGMSG_ERR_DBI_EDIT_GENERIC = "Edit record error: %s" +
        " Database edit generic error, format with err message "
const val LOGMSG_ERR_DBI_DEL_GENERIC = "Delete record error: %s" +
        " Database delete generic error, format with err message "
const val LOGMSG_WAR_DBI_AVG_ZERODIV = "Zero division on aggregate_avg"

const val LOGMSG_WAR_FAB_VIEW_EXISTS = "View already exists %s ignoring" +
        " Attempt to add an already added view, format with view name "
const val LOGMSG_WAR_DBI_ADD_INTEGRITY = "Add record integrity error: %s" +
        " Dabase integrity error, format with err message "
const val LOGMSG_WAR_DBI_EDIT_INTEGRITY = "Edit record integrity error: %s" +
        " Dabase integrity error, format with err message "
const val LOGMSG_WAR_DBI_DEL_INTEGRITY = "Delete record integrity error: %s" +
        " Dabase integrity error, format with err message "

const val LOGMSG_INF_FAB_ADD_VIEW = "Registering class %s on menu %s" +
        " Inform that view class was added, format with class name, name"

