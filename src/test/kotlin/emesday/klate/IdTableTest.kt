package emesday.klate

import org.jetbrains.exposed.dao.id.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.*
import org.jetbrains.exposed.sql.transactions.*
import java.time.*
import java.util.*
import kotlin.random.*
import kotlin.streams.*
import kotlin.test.*

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

object Names {

    private val names = javaClass.getResourceAsStream("/NAMES.DIC")!!.bufferedReader().lines().toList()
    private val r = Random(442)

    fun random(size: Int = 1): String {
        return (0 until size).joinToString(" ") {
            names[r.nextInt(0..names.size)].replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }

    fun randomBirthday(): LocalDate {
        val year = r.nextInt(1900..2012)
        val month = r.nextInt(1..12)
        val day = r.nextInt(1..28)
        return LocalDate.of(year, month, day)

    }
}

class IdTableTest {

    @BeforeTest
    fun beforeTest() {
//        DatabaseFactory.init(File.createTempFile("klate-test", ".db").absolutePath)

        transaction {
            SchemaUtils.create(ContactGroup)
            SchemaUtils.create(Gender)
            SchemaUtils.create(Contact)
            ContactGroup.batchInsert(listOf("Friends", "Family", "Work")) {
                this[ContactGroup.name] = it
            }
            Gender.batchInsert(listOf("Male", "Female")) {
                this[Gender.name] = it
            }

            for (row in ContactGroup.selectAll()) {
                println(row)
            }

            for (row in Gender.selectAll()) {
                println(row)
            }
        }
    }

    @Test
    fun `insert test data`() {
        val expected = 50L
        transaction {
            val r = Random(443)
            Contact.batchInsert(
                0 until expected
            ) {
                this[Contact.name] = Names.random(r.nextInt(2..5))
                this[Contact.address] = "Street ${Names.random()}"
                this[Contact.birthday] = Names.randomBirthday()
                this[Contact.personalPhone] = r.nextInt(111111..9999999).toString()
                this[Contact.personalCellPhone] = r.nextInt(111111..9999999).toString()
                this[Contact.contactGroup] = r.nextInt(1..3)
                this[Contact.gender] = r.nextInt(1..2)
            }
        }

        val actual = transaction {
            Contact.selectAll().count()
        }

        assertEquals(expected, actual)
    }
}