package emesday.klate

import emesday.klate.config.*
import emesday.klate.security.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import io.ktor.util.*

const val MODEL1_DATA_SIZE = 30
const val MODEL2_DATA_SIZE = 30
const val MODELOMCHILD_DATA_SIZE = 30

const val USERNAME_ADMIN = "testadmin"
const val PASSWORD_ADMIN = "password"
const val MAX_PAGE_SIZE = 25
const val USERNAME_READONLY = "readonly"
const val PASSWORD_READONLY = "readonly"

@KtorDsl
fun TestApplicationBuilder.klateEnvironment(block: MapApplicationConfig.() -> Unit = {}) {
    environment {
        buildEnvironment(block)
    }
}

fun ApplicationEngineEnvironmentBuilder.buildEnvironment(block: MapApplicationConfig.() -> Unit = {}) {
    val config = MapApplicationConfig()
    config.klate.auth.oauth.providers = emptyList()
    config.klate.db.driver = "org.h2.Driver"
    config.klate.db.url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    config.apply(block)

    this.config = config
}

fun BaseSecurityManager.addTestUsersAndRoles() {
    val adminRole = addRole("Admin")
    val publicRole = addRole("Public")
    addUser(USERNAME_ADMIN, "", "", "admin@example.com", listOfNotNull(adminRole), PASSWORD_ADMIN)
    addUser(USERNAME_READONLY, "", "", "readonly@example.com", listOfNotNull(publicRole), PASSWORD_READONLY)
}