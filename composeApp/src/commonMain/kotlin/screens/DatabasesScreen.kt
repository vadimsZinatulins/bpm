package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import utils.storage.FileManager
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.GenericDialog
import data.Database
import data.NewDatabase
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import utils.DatabasesManager
import utils.ServiceLocator

class DatabasesScreen : Screen {

    private val fileManager by lazy { ServiceLocator.getService<FileManager>("FileManager") }

    @Composable
    override fun Content() {
        var databases by remember { mutableStateOf(fileManager.listFiles().map { it.name }) }

        var newDatabaseName by remember { mutableStateOf("") }

        Column(modifier = Modifier.fillMaxSize()) {
            ListDatabases(databases) { databaseToDelete ->
                fileManager.deleteFile(databaseToDelete)

                databases = fileManager.listFiles().map { it.name }
            }

            OutlinedTextField(
                value = newDatabaseName,
                onValueChange = { newDatabaseName = it},
                label = { Text("Database Name") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Button(onClick = {
                // create new database
                val newDatabase = NewDatabase(newDatabaseName)

                // save new database
                fileManager.saveDatabase(newDatabase)

                // refresh databases
                databases = fileManager.listFiles().map { it.name }
            }) {
                Icon(Icons.Default.Add, "New Database")
            }
        }
    }

    @Composable
    private fun ListDatabases(databases: List<String>, onDatabaseDelete: (database: String) -> Unit) {
        LazyColumn {
            items(databases) { database ->
                DatabaseRow(database, onDatabaseDelete)
            }
        }
    }

    @Composable
    private fun DatabaseRow(database: String, onDelete: (database: String) -> Unit) {
        val navigator = LocalNavigator.currentOrThrow

        Row {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Database",
                tint = Color.Red,
                modifier = Modifier.clickable { onDelete(database) }
            )

            Text(
                text = database.split(".").first(),
                modifier = Modifier.clickable {
                    val db = fileManager.loadDatabase(database)
                    navigator.replace(DatabaseScreen(db))
                }
            )
        }
    }
}