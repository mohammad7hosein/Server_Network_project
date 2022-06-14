import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect
import java.net.ServerSocket
import java.nio.charset.Charset
import kotlin.concurrent.thread

@ExperimentalUnitApi
fun main() = application {

    val server = ServerSocket(9999)
    println("Server is running on port ${server.localPort}")
    val client = server.accept()
    println("Client connected: ${client.inetAddress.hostAddress}")
    val writer = client.getOutputStream()

    Window(
        icon = painterResource("img.png"),
        onCloseRequest = ::exitApplication, title = "Server"
    ) {
        var inputValue by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "خوش آمدید!",
                style = TextStyle(
                    fontSize = TextUnit(32f, TextUnitType.Sp),
                    fontFamily = FontFamily(
                        Font(resource = "vazir.ttf", weight = FontWeight.Bold)
                    ),
                    textAlign = TextAlign.Right,
                    textDirection = TextDirection.Rtl
                )
            )
            Spacer(modifier = Modifier.weight(40f))
            OutlinedTextField(
                value = inputValue,
                onValueChange = {
                    if (it.length <= 2)
                        inputValue = it
                },
                label = {
                    Text(
                        text = "تعداد دانشکده",
                        style = TextStyle(
                            textAlign = TextAlign.Right,
                            textDirection = TextDirection.Rtl,
                            fontFamily = FontFamily(
                                Font(resource = "vazir.ttf")
                            )
                        ),
                    )
                })
            Spacer(modifier = Modifier.weight(20f))
            Button(
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    writer.write((inputValue + '\n').toByteArray(Charset.defaultCharset()))
                    runServer(server, inputValue.toInt())
                }
            ) {
                Text(
                    text = "تایید",
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(resource = "vazir.ttf", weight = FontWeight.Bold)
                        )
                    )
                )
            }
        }
    }

}

fun runServer(server: ServerSocket, clientNumber: Int) {

    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/network_project",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "@Mohamad77",
        dialect = MySqlDialect()
    )

    var clientNumber = clientNumber
    while (clientNumber > 0) {
        clientNumber--
        val client = server.accept()
        println("Client connected: ${client.inetAddress.hostAddress}")
        thread { ClientHandler(client).run(database) }
    }

}
