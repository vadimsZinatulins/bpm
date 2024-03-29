import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import utils.ServiceLocator
import utils.storage.DesktopFileManager
import utils.ui.CredentialDecoratorService
import utils.ui.GroupsDecoratorService

fun main() = application {
    ServiceLocator.registerService(DesktopFileManager(), "FileManager")
    ServiceLocator.registerService(GroupsDecoratorService(), "GroupsDecorator")
    ServiceLocator.registerService(CredentialDecoratorService(), "CredentialDecorator")

    Window(onCloseRequest = ::exitApplication, title = "Basic Password Manager") {
        App()
    }
}