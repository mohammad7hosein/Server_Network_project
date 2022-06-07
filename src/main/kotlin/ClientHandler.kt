import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

class ClientHandler(client: Socket) {
    private val client: Socket = client
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private var running: Boolean = false

    fun run() {
        running = true
        while (running) {
            try {
                val text = reader.nextLine()
                if (text == "exit") {
                    shutdown()
                    continue
                }
                println("from client: $text")
            } catch (e: Exception) {
                e.printStackTrace()
                shutdown()
            } finally {

            }
        }
    }

     fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun shutdown() {
        running = false
        client.close()
        println("${client.inetAddress.hostAddress} closed the connection")
    }

}