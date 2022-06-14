import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler(client: Socket) {
    private val client: Socket = client
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private var running: Boolean = false

    fun run(database: Database) {
        running = true
        while (running) {
            try {
                val clientId = reader.nextLine().toInt()
                val text = reader.nextLine()
                when (text) {
                    "min" -> {
                        val query = database.from(Students)
                            .select(Students.firstName, Students.lastName, Students.nationalCode, Students.average)
                            .where { Students.clientId eq clientId }.orderBy(Students.average.asc()).limit(1)
                        write("1")
                        query.forEach { row ->
                            write("${row[Students.firstName]},${row[Students.lastName]},${row[Students.nationalCode]},${row[Students.average]}")
                        }
                    }
                    "max" -> {
                        val query = database.from(Students)
                            .select(Students.firstName, Students.lastName, Students.nationalCode, Students.average)
                            .where { Students.clientId eq clientId }.orderBy(Students.average.desc()).limit(1)
                        write("1")
                        query.forEach { row ->
                            write("${row[Students.firstName]},${row[Students.lastName]},${row[Students.nationalCode]},${row[Students.average]}")
                        }
                    }
                    "avg" -> {
                        val query = database.from(Students)
                            .select(Students.firstName, Students.lastName, Students.nationalCode, Students.average)
                            .where { Students.clientId eq clientId }
                        write(query.totalRecords.toString())
                        query.forEach { row ->
                            write("${row[Students.firstName]},${row[Students.lastName]},${row[Students.nationalCode]},${row[Students.average]}")
                        }
                    }
                    "sort" -> {
                        val query = database.from(Students)
                            .select(Students.firstName, Students.lastName, Students.nationalCode, Students.average)
                            .where { Students.clientId eq clientId }.orderBy(Students.average.asc())
                        write(query.totalRecords.toString())
                        query.forEach { row ->
                            write("${row[Students.firstName]},${row[Students.lastName]},${row[Students.nationalCode]},${row[Students.average]}")
                        }
                    }
                    "student" -> {
                        database.insert(Students) {
                            set(it.clientId, clientId)
                            set(it.firstName, reader.nextLine())
                            set(it.lastName, reader.nextLine())
                            set(it.nationalCode, reader.nextLine())
                            set(it.identityNumber, reader.nextLine())
                            set(it.courseOne, reader.nextLine().toFloat())
                            set(it.courseTwo, reader.nextLine().toFloat())
                            set(it.courseThree, reader.nextLine().toFloat())
                            set(it.courseFour, reader.nextLine().toFloat())
                            set(it.courseFive, reader.nextLine().toFloat())
                            set(
                                it.average,
                                (it.courseOne + it.courseTwo + it.courseThree + it.courseFour + it.courseFive) / 5f
                            )
                        }
                    }
                    else -> println("Invalid Input")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                shutdown()
            }
        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

}