package emesday.quickhowto2.models

import emesday.quickhowto2.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.*

data class Article(val id: Long, val title: String, val body: String)

object Articles : LongIdCrudTable<Article>() {
    val title = varchar("title", 128)
    val body = varchar("body", 1024)

    override fun resultRowToModel(row: ResultRow): Article = Article(
        id = row[id].value,
        title = row[title],
        body = row[body]
    )

    override fun insertStatement(statement: InsertStatement<Number>, model: Article): InsertStatement<Number> {
        statement[title] = model.title
        statement[body] = model.body
        return statement
    }

    override fun updateStatement(statement: UpdateStatement, model: Article): UpdateStatement {
        statement[title] = model.title
        statement[body] = model.body
        return statement
    }
}
