package emesday.klate.security

enum class AuthType(val value: Int) {
    OID(0),
    DB(1),
    LDAP(2),
    REMOTE_USER(3),
    OAUTH(4)
}