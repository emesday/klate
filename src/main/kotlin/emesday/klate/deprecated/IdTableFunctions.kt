package emesday.klate

import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*

fun <T : Comparable<T>> IdTable<T>.isPrimaryKey(column: Column<*>): Boolean {
    return primaryKey?.columns?.contains(column) ?: false
}

fun <T : Comparable<T>> IdTable<T>.isForeignKey(column: Column<*>): Boolean {
    return column.foreignKey != null
}

fun <T : Comparable<T>> IdTable<T>.getUserColumnsList(): List<String> {
    return columns.filterNot { isPrimaryKey(it) or isForeignKey(it) }.map { it.name }
}