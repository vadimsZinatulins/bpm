import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import utils.storage.FileManager
import utils.storage.IFileManager
import java.io.File

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Basic Password Manager") {
        App(FileManager())
    }
}