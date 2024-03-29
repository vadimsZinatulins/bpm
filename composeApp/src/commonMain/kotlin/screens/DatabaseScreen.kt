package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Database
import utils.ServiceLocator
import utils.storage.FileManager

// @OptIn(ExperimentalFoundationApi::class)
class DatabaseScreen(val database: Database) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column {
            Row {
                Text("Database: ${database.name}")
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Database",
                    modifier = Modifier.clickable {
                        val fileManager = ServiceLocator.getService<FileManager>("FileManager")
                        fileManager.deleteDatabase(database)

                        navigator.replace(DatabasesScreen())
                    }
                )
            }
            ChangeName()
        }
    }

    @Composable
    private fun ChangeName() {
        val navigator = LocalNavigator.currentOrThrow

        var newName by remember { mutableStateOf("") }

        Row {
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("New name") }
            )
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Change name",
                modifier = Modifier.clickable {
                    val newDatabase = changeDatabaseName(newName)
                    navigator.replace(DatabaseScreen(newDatabase))

                    newName = ""
                }
            )
        }

    }

    private fun changeDatabaseName(newName: String) : Database {
        val fileManager = ServiceLocator.getService<FileManager>("FileManager")

        val newDatabase = Database(newName)

        fileManager.saveDatabase(newDatabase)
        fileManager.deleteDatabase(database)

        return newDatabase
    }

    /*
    @Composable
    override fun Content() {
        val state = rememberLazyListState()
        val scope = rememberCoroutineScope()

        val scrollTo = { index: Int ->
            scope.launch {
                state.animateScrollToItem(index, 0)
            }
        }

        BoxWithConstraints {
            if (this.maxWidth > 600.dp) {
                Row {
                    Groups(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(300.dp)
                            .background(Color.Blue.copy(alpha = 0.05f)),
                        {}
                    )
                    Credentials(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(Color.Blue.copy(alpha = 0.025f)),
                        null
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                    state = state,
                    flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
                ) {
                    item {
                        Groups(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(this@BoxWithConstraints.maxWidth)
                                .background(Color.Blue.copy(alpha = 0.05f)),
                            {}
                        ) {
                            scrollTo(1)
                        }
                    }
                    item {
                        Credentials(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(this@BoxWithConstraints.maxWidth)
                                .background(Color.Blue.copy(alpha = 0.025f)),
                            null
                        ) {
                            scrollTo(0)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun Groups(
        modifier: Modifier = Modifier,
        onSelectedChange: (Group) -> Unit,
        onClick: (() -> Unit)? = null
    ) {
        val groupDecoratorService =
            ServiceLocator.getService<IGroupsDecoratorService>("GroupsDecorator")

        val groups = listOf(
            Group("Group 1", listOf(
                Group("Group 1", emptyList(), emptyList()),
                Group("Group 2", emptyList(), emptyList()),
                Group("Group 3", emptyList(), emptyList()),
            ), listOf(
                Credential("Credential 1", "username", "password", ""),
            )),
            Group("Group 2", emptyList(), emptyList()),
            Group("Group 3", emptyList(), emptyList()),
            Group("Group 4", emptyList(), emptyList()),
        )

        Column(modifier) {
            onClick?.let {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ArrowCircleRight,
                        "To Credentials",
                        tint = Color(0xFFB3E5FC),
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { it() }
                            .border(2.dp, Color(0xFF7AAFD3), CircleShape)
                    )
                }
            }

            groupDecoratorService.decorate(groups, {  })()
        }
    }

    @Composable
    private fun Credentials(
        modifier: Modifier = Modifier,
        selectedGroup: Group?,
        onClick: (() -> Unit)? = null
    ) {
        val decorator = ServiceLocator
            .getService<ICredentialDecoratorService>("CredentialDecorator")

        Column(modifier) {
            onClick?.let {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.ArrowCircleLeft,
                        contentDescription = "To Groups",
                        tint = Color(0xFFB3E5FC),
                        modifier = Modifier
                            .size(38.dp)
                            .clickable { it() }
                            .border(2.dp, Color(0xFF7AAFD3), CircleShape)
                    )
                }
                selectedGroup?.let {
                }
            }
        }
    }
    */
}