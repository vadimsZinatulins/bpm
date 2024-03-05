import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import utils.storage.DesktopFileManager

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Basic Password Manager") {
        App(DesktopFileManager())
    }
}