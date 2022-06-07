import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class MyServer {
    lateinit var server: ServerSocket
    lateinit var client: Socket
    lateinit var output: PrintWriter
    lateinit var input: BufferedReader

    fun start(port: Int) {
        server = ServerSocket(port)
        println("Server running on port ${server.localPort}")
        client = server.accept()
        output = PrintWriter(client.getOutputStream())
        input = BufferedReader(InputStreamReader(client.getInputStream()))
        println("Client connected to server: ${client.inetAddress.hostAddress}")
//        handleMessage()
    }

    fun sendClientNumber(clientNumber: Int) {
        output.println(clientNumber)
        println(clientNumber)
    }

    fun handleMessage() {
        val message = input.readLine()
        println("Server receiving [$message]")
        val response = when (message) {
            "hello server!" -> "hello client"
            else -> "danger ... friend"
        }
        output.println(response)
        println("Server responding [$response]")
    }

    fun stop() {
        try {
            input.close()
            output.close()
            server.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}