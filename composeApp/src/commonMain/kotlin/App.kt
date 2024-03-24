import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import components.Background
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.DatabasesListScreen
import screens.Home
import utils.storage.FileManager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Database
import screens.DatabaseScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val startingScreen = Home()
        // val startingScreen = DatabasesListScreen(fileManager)

        Navigator(startingScreen) { navigator ->
            var bottomBarSize by remember { mutableStateOf(IntSize.Zero) }

            Scaffold(
                topBar = {
                    Text(navigator.lastItem.key.split(".").last(), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                },
                bottomBar = {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().background(Color.White).padding(4.dp, 0.dp).onSizeChanged { bottomBarSize = it },
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val lastItem = navigator.lastItem

                        Database.databases.filter { database -> database.isOpen } .map {
                            val name = it.fileName.split('.').first()
                            val icon = Icons.Default.Storage
                            val isSelected = lastItem is DatabaseScreen && lastItem.database.fileName == it.fileName

                            item { BottomBarButton(name, icon, isSelected) { navigator.replace(DatabaseScreen(it)) } }
                        }

                        item { BottomBarButton("Open", Icons.Default.Lock, isSelected = lastItem is DatabasesListScreen ) { navigator.replace(DatabasesListScreen()) } }
                        item { BottomBarButton("Settings", Icons.Default.Settings) {  } }
                    }
                }
            ) {
                Box(modifier = Modifier.fillMaxSize()/*.padding(bottom = bottomBarSize.height.dp)*/) {
                    Background()
                    CurrentScreen()
                }
            }
        }
    }
}

@Composable
fun BottomBarButton(text: String, icon: ImageVector, isSelected: Boolean = false, onClick: () -> Unit) {
    val columnOffset = if(isSelected) (-6).dp else 0.dp
    val textColor = if(isSelected) Color.Black else Color.LightGray
    val iconTint = if(isSelected) Color.White else Color.LightGray
    val iconModifier = if(isSelected) Modifier
        .background(Color(0xFF9FCFE6), CircleShape)
        .border(2.dp, Color.White, CircleShape)
    else Modifier

    Column(
        modifier = Modifier
            .offset(y = columnOffset)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(icon, text, modifier = iconModifier.size(38.dp).padding(6.dp), tint = iconTint)
        Text(text = text, fontSize = 12.sp, color = textColor)
    }
}
