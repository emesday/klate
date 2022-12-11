package emesday.klate.deprecated.appbuilder.security

interface BaseSecurityManager {

    fun getUserMenuAccess(menuNames: List<String>): List<String>
}