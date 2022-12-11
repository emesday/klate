package emesday.quickhowto2

import emesday.klate.api.*
import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.*
import org.jetbrains.exposed.sql.transactions.*

object ContactGroup : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
}

object Gender : IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
}

object Contact : IntIdTable() {
    val name = varchar("name", 150).uniqueIndex()
    val address = varchar("address", 564).nullable()
    val birthday = date("birthday").nullable()
    val personalPhone = varchar("personal_phone", 20).nullable()
    val personalCellPhone = varchar("personal_cellphone", 20).nullable()
    val contactGroup = reference("contactGroup", ContactGroup)
    val gender = reference("gender", Gender)
}

fun fillGender() {
    transaction {
        Gender.batchInsert(listOf("Male", "Female", "Nonbinary")) {
            this[Gender.name] = it
        }
    }
}

fun customFilter(): BaseFilter {
    return BaseFilter()
}


val ContactApi = createModelApi(Contact) {
    resourceName = "contact"
//    allowBrowserLogin = true
//    searchFilters = mapOf("name" to listOf(customFilter()))
}

val ContactGroupApi = createModelApi(ContactGroup) {
    resourceName = "group"
//    allowBrowserLogin = true
}
