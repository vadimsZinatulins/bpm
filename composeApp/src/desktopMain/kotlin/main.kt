import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import utils.ServiceLocator
import utils.storage.DesktopFileManager

fun main() = application {
    ServiceLocator.registerService(DesktopFileManager(), "FileManager")

    Window(onCloseRequest = ::exitApplication, title = "Basic Password Manager") {
        App()
    }
}