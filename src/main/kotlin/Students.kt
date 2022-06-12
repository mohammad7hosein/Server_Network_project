import org.ktorm.schema.Table
import org.ktorm.schema.float
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Students : Table<Nothing>("student_table") {
    val id = int("id").primaryKey()
    val clientId = int("client_id")
    val firstName = varchar("first_name")
    val lastName = varchar("last_name")
    val nationalCode = varchar("national_code")
    val identityNumber = varchar("identity_number")
    val courseOne = float("course_one")
    val courseTwo = float("course_two")
    val courseThree = float("course_three")
    val courseFour = float("course_four")
    val courseFive = float("course_five")
    val average = float("average")
}