import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.ServerSocket
import java.nio.charset.Charset
import java.util.*

fun main() = application {
    val scope = rememberCoroutineScope()

        val server = ServerSocket(9999)
        println("Server is running on port ${server.localPort}")
        val client = server.accept()
        println("Client connected: ${client.inetAddress.hostAddress}")
        val reader = Scanner(client.getInputStream())
        val writer = client.getOutputStream()

    Window(onCloseRequest = ::exitApplication, title = "Server") {
        var inputValue by remember { mutableStateOf("") }
        DesktopMaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text("Welcome!")
                Spacer(modifier = Modifier.weight(40f))
                OutlinedTextField(value = inputValue, onValueChange = { inputValue = it })
                Spacer(modifier = Modifier.weight(20f))
                Button(
                    onClick = {
                        writer.write((inputValue + '\n').toByteArray(Charset.defaultCharset()))
                    }
                ) {
                    Text("تایید")
                }
            }
        }
    }


}
