package emesday.klate.security

import io.ktor.server.auth.*

data class UserSession(val username: String, val isAuthenticated: Boolean, val count: Int) : Principal
