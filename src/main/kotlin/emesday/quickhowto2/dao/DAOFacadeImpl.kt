package emesday.quickhowto2.dao

import emesday.quickhowto2.dao.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class DAOFacadeImpl<T : Comparable<T>, M>(private val table: CrudTable<T, M>) : DAOFacade<T, M> {


    override suspend fun getAll(): List<M> = dbQuery {
        table.selectAll().map { table.resultRowToModel(it) }
    }

    override suspend fun get(id: T): M? = dbQuery {
        table
            .select { table.id eq id }
            .map { table.resultRowToModel(it) }
            .singleOrNull()
    }

    override suspend fun create(item: M): M? = dbQuery {
        val insertStatement = table.insert { table.insertStatement(it, item) }
        insertStatement.resultedValues?.singleOrNull()?.let { table.resultRowToModel(it) }
    }

    override suspend fun update(id: T, item: M): Boolean = dbQuery {
        table.update({ table.id eq id }) {
            table.updateStatement(it, item)
        } > 0
    }

    override suspend fun delete(id: T): Boolean = dbQuery {
        table.deleteWhere { table.id eq id } > 0
    }
}
