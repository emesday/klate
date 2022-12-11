package emesday.klate.database

import emesday.klate.config.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*

fun Application.configureDatabase() {
    val url = environment.config.klate.db.url
    val driverClassName = environment.config.klate.db.driver
    when {
        url != null && driverClassName == null ->
            Database.connect(url)
        url != null && driverClassName != null ->
            Database.connect(url, driverClassName)
        else -> {}
    }
}
