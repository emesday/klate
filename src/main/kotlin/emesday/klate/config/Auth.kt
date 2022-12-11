package emesday.klate.config

import emesday.klate.exceptions.*
import emesday.klate.security.*
import io.ktor.server.config.*

class KlateApplicationConfigAuthApi(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    var loginAllowMultipleProviders: Boolean
        get() = ac.boolean(this.combine("loginAllowMultipleProviders")) ?: false
        set(value) {
            ac.put(this.combine("loginAllowMultipleProviders"), value)
        }
}

class KlateApplicationConfigAuthUser(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    // properties

    var registration: Boolean
        get() = ac.boolean(combine("registration")) ?: false
        set(value) {
            ac.put(combine("registration"), value)
        }

    var registrationRole: String
        get() = ac.string(combine("registrationRole")) ?: klate.auth.role.public
        set(value) {
            ac.put(combine("registrationRole"), value)
        }

    var registrationRoleJMESPath: String?
        get() = ac.string(combine("registrationRoleJMESPath"))
        set(value) {
            ac.put(combine("registrationRoleJMESPath"), value)
        }
}

class KlateApplicationConfigAuthRoles(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    var syncAtLogin: Boolean
        get() = ac.boolean(combine("syncAtLogin")) ?: false
        set(value) {
            ac.put(combine("syncAtLogin"), value)
        }

    var mapping: Map<String, List<String>>
        get() = mapGetter("mapping")
        set(value) = mapSetter("mapping", value)
}

class KlateApplicationConfigAuthRole(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    var admin: String
        get() = ac.string(combine("admin")) ?: "Admin"
        set(value) {
            ac.put(combine("admin"), value)
        }

    var public: String
        get() = ac.string(combine("public")) ?: "Public"
        set(value) {
            ac.put(combine("public"), value)
        }
}

class KlateApplicationConfigAuthOAuth(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    var providers: List<String>
        get() = ac.getList(this.combine("providers")) ?: emptyList()
        set(value) {
            ac.put(this.combine("providers"), value)
        }
}

class KlateApplicationConfigAuthLdap(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    var server: String
        get() = ac.string(this.combine("server")) ?: throw KlateException(
            "No ${combine("server")} defined on config with ${AuthType.LDAP} authentication type."
        )
        set(value) {
            ac.put(this.combine("server"), value)
        }

    var search: String
        get() = ac.string(this.combine("search")) ?: ""
        set(value) {
            ac.put(this.combine("search"), value)
        }

    var searchFilter: String
        get() = ac.string(this.combine("searchFilter")) ?: ""
        set(value) {
            ac.put(this.combine("searchFilter"), value)
        }

    var appendDomain: String
        get() = ac.string(this.combine("appendDomain")) ?: ""
        set(value) {
            ac.put(this.combine("appendDomain"), value)
        }

    var usernameFormat: String
        get() = ac.string(this.combine("usernameFormat")) ?: ""
        set(value) {
            ac.put(this.combine("usernameFormat"), value)
        }

    var bindUser: String
        get() = ac.string(this.combine("bindUser")) ?: ""
        set(value) {
            ac.put(this.combine("bindUser"), value)
        }

    var bindPassword: String
        get() = ac.string(this.combine("bindPassword")) ?: ""
        set(value) {
            ac.put(this.combine("bindPassword"), value)
        }

    var useTls: Boolean
        get() = ac.boolean(this.combine("useTls")) ?: false
        set(value) {
            ac.put(this.combine("useTls"), value)
        }

    var allowSelfSigned: Boolean
        get() = ac.boolean(this.combine("allowSelfSigned")) ?: false
        set(value) {
            ac.put(this.combine("allowSelfSigned"), value)
        }

    var tlsDemand: Boolean
        get() = ac.boolean(this.combine("tlsDemand")) ?: false
        set(value) {
            ac.put(this.combine("tlsDemand"), value)
        }

    var tlsCACertDir: String
        get() = ac.string(this.combine("tlsCACertDir")) ?: ""
        set(value) {
            ac.put(this.combine("tlsCACertDir"), value)
        }

    var tlsCACertFile: String
        get() = ac.string(this.combine("tlsCACertFile")) ?: ""
        set(value) {
            ac.put(this.combine("tlsCACertFile"), value)
        }

    var tlsCertFile: String
        get() = ac.string(this.combine("tlsCertFile")) ?: ""
        set(value) {
            ac.put(this.combine("tlsCertFile"), value)
        }

    var tlsKeyFile: String
        get() = ac.string(this.combine("tlsKeyFile")) ?: ""
        set(value) {
            ac.put(this.combine("tlsKeyFile"), value)
        }

    var uidField: String
        get() = ac.string(this.combine("uidField")) ?: "uid"
        set(value) {
            ac.put(this.combine("uidField"), value)
        }

    var groupField: String
        get() = ac.string(this.combine("groupField")) ?: "memberOf"
        set(value) {
            ac.put(this.combine("groupField"), value)
        }

    var firstNameField: String
        get() = ac.string(this.combine("firstNameField")) ?: "givenName"
        set(value) {
            ac.put(this.combine("firstNameField"), value)
        }

    var lastNameField: String
        get() = ac.string(this.combine("lastNameField")) ?: "sn"
        set(value) {
            ac.put(this.combine("lastNameField"), value)
        }

    var emailField: String
        get() = ac.string(this.combine("emailField")) ?: "mail"
        set(value) {
            ac.put(this.combine("emailField"), value)
        }
}

class KlateApplicationConfigAuth(
    override val ac: ApplicationConfig,
    override val path: String,
) : KlateApplicationConfigChild {

    private var userInstance: KlateApplicationConfigAuthUser? = null

    private var rolesInstance: KlateApplicationConfigAuthRoles? = null

    private var roleInstance: KlateApplicationConfigAuthRole? = null

    private var oauthInstance: KlateApplicationConfigAuthOAuth? = null

    private var ldapInstance: KlateApplicationConfigAuthLdap? = null

    private var apiInstance: KlateApplicationConfigAuthApi? = null

    val user: KlateApplicationConfigAuthUser
        get() = run {
            if (userInstance == null) {
                userInstance = KlateApplicationConfigAuthUser(ac, combine("user"))
            }
            userInstance!!
        }

    val roles: KlateApplicationConfigAuthRoles
        get() = run {
            if (rolesInstance == null) {
                rolesInstance = KlateApplicationConfigAuthRoles(ac, combine("roles"))
            }
            rolesInstance!!
        }

    val role: KlateApplicationConfigAuthRole
        get() = run {
            if (roleInstance == null) {
                roleInstance = KlateApplicationConfigAuthRole(ac, combine("role"))
            }
            roleInstance!!
        }

    val oauth: KlateApplicationConfigAuthOAuth
        get() = run {
            if (oauthInstance == null) {
                oauthInstance = KlateApplicationConfigAuthOAuth(ac, combine("oauth"))
            }
            oauthInstance!!
        }

    val ldap: KlateApplicationConfigAuthLdap
        get() = run {
            if (ldapInstance == null) {
                ldapInstance = KlateApplicationConfigAuthLdap(ac, combine("ldap"))
            }
            ldapInstance!!
        }

    val api: KlateApplicationConfigAuthApi
        get() = run {
            if (apiInstance == null) {
                apiInstance = KlateApplicationConfigAuthApi(ac, combine("api"))
            }
            apiInstance!!
        }

    // properties

    var type: AuthType
        get() = ac.string(combine("type"))?.uppercase()?.let { AuthType.valueOf(it) } ?: AuthType.DB
        set(value) {
            ac.put(combine("type"), value.name)
        }

    var usernameCI: Boolean
        get() = ac.boolean(combine("usernameCI")) ?: true
        set(value) {
            ac.put(combine("usernameCI"), value)
        }
}