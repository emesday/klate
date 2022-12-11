package emesday.quickhowto2.dao

import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.*
import java.util.*

abstract class CrudTable<T : Comparable<T>, M>(name: String = "") : IdTable<T>(name) {

    abstract fun resultRowToModel(row: ResultRow): M

    abstract fun insertStatement(statement: InsertStatement<Number>, model: M): InsertStatement<Number>

    abstract fun updateStatement(statement: UpdateStatement, model: M): UpdateStatement
}

abstract class IntIdCrudTable<M>(name: String = "", columnName: String = "id") : CrudTable<Int, M>(name) {
    final override val id: Column<EntityID<Int>> = integer(columnName).autoIncrement().entityId()
    final override val primaryKey = PrimaryKey(id)
}

abstract class LongIdCrudTable<M>(name: String = "", columnName: String = "id") : CrudTable<Long, M>(name) {
    final override val id: Column<EntityID<Long>> = long(columnName).autoIncrement().entityId()
    final override val primaryKey = PrimaryKey(id)
}

abstract class UUIDCrudTable<M>(name: String = "", columnName: String = "id") : CrudTable<UUID, M>(name) {
    final override val id: Column<EntityID<UUID>> = uuid(columnName).autoGenerate().entityId()
    final override val primaryKey = PrimaryKey(id)
}
