package emesday.quickhowto2.dao

import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init(path: String = "./build/db") {
        val driverClassName = "org.h2.Driver"
        val jdbcURL = "jdbc:h2:file:$path"
        Database.connect(jdbcURL, driverClassName)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}