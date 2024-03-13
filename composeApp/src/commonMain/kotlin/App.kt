import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        modifier = Modifier.fillMaxWidth().background(Color.White).padding(4.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        println("Updating Bottom Bar...")

                        Database.databases
                            .filter { database ->
                                var result = database.isOpen

                                if(navigator.lastItem is DatabaseScreen) {
                                    result = result && (navigator.lastItem as DatabaseScreen).database.fileName == database.fileName
                                }

                                result
                            }
                            .map {
                                item {BottomBarButton(it.fileName.split('.').first(), Icons.Default.Lock, navigator.lastItem is DatabaseScreen) { navigator.push(DatabaseScreen(it)) }
                            }
                        }

                        item { BottomBarButton("Open", Icons.Default.Lock, isSelected = navigator.lastItem is DatabasesListScreen ) { navigator.push(DatabasesListScreen(fileManager)) } }
                        item { BottomBarButton("Settings", Icons.Default.Settings) {  } }
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
fun BottomBarButton(text: String, icon: ImageVector, isSelected: Boolean = false, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if(isSelected) {
            Icon(
                icon,
                text,
                modifier = Modifier
                    // .background(Color.Red)
                    .offset(y = (-6).dp)
                    .background(Color(0xFF9FCFE6), CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .size(38.dp)
                    .padding(6.dp),
                tint = Color.White
            )
        } else {
            Icon(
                icon,
                text,
                modifier = Modifier
                    // .background(Color.Red)
                    .size(38.dp)
                    .padding(6.dp),
                tint = Color.LightGray
            )
        }
        Text(text = text, modifier = Modifier.offset(y = (-6).dp), fontSize = 12.sp, color = if(isSelected) Color.Black else Color.LightGray)
    }
}
