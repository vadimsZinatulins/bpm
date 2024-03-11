import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import components.Background
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.DatabasesListScreen
import screens.Home
import utils.storage.FileManager
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import data.Database
import screens.DatabaseScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(
    fileManager: FileManager
) {
    MaterialTheme {
        val startingScreen = Home()
        // val startingScreen = DatabasesListScreen(fileManager)

        Navigator(startingScreen) { navigator ->
            Scaffold(
                topBar = {
                    Text(navigator.lastItem.key.split(".").last(), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                },
                bottomBar = {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().background(Color(0xFFE0F2F1)).padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        Database.databases.filter { database ->
                            var result = database.isOpen
                            if (navigator.lastItem is DatabaseScreen) {
                                val screen = navigator.lastItem as DatabaseScreen
                                result = result && screen.database.fileName != database.fileName
                            }

                            result
                        } .map {
                            item { BottomBarButton(it.fileName.split('.').first()) { navigator.push(DatabaseScreen(it)) } }
                        }

                        if (navigator.lastItem !is DatabasesListScreen) {
                            item { BottomBarButton("Open Database") { navigator.push(DatabasesListScreen(fileManager)) } }
                        }

                        item { BottomBarButton("Settings") { /* TODO */ } }
                    }
                }
            ) {
                Background()
                CurrentScreen()
            }
        }
    }
}

@Composable
fun BottomBarButton(text: String, onClick: () -> Unit) {
    val textSize = 15
    val displayedText = if(text.length > textSize) text.take(textSize) + "..." else text

    Button(onClick = onClick, modifier = Modifier.width(180.dp)) { Text(displayedText) }
}
