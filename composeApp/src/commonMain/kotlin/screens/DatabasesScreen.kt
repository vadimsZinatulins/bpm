package screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import utils.storage.FileManager
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.Database
import utils.ServiceLocator

class DatabasesScreen : Screen {

    private val fileManager by lazy { ServiceLocator.getService<FileManager>("FileManager") }

    @Composable
    override fun Content() {
        var databases by remember { mutableStateOf(fileManager.listFiles().map { it.name }) }

        Column(modifier = Modifier.fillMaxSize()) {
            ListDatabases(databases) { databaseToDelete ->
                fileManager.deleteFile(databaseToDelete)

                databases = fileManager.listFiles().map { it.name }
            }
            NewDatabaseButton()

        }
    }

    @Composable
    private fun ListDatabases(
        databases: List<String>,
        onDatabaseDelete: (database: String) -> Unit
    ) {
        LazyColumn {
            items(databases) { database ->
                DatabaseRow(database, onDatabaseDelete)
            }
        }
    }

    @Composable
    private fun DatabaseRow(database: String, onDelete: (database: String) -> Unit) {
        var showDialog by remember { mutableStateOf(false) }

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
                modifier = Modifier.clickable { showDialog = true }
            )

            OpenDatabaseDialog(
                showDialog = showDialog,
                onConfirm = { password ->
                    val db = fileManager.loadDatabase(database, password)
                    navigator.replace(DatabaseScreen(db))
                },
                onCancel = { showDialog = false }
            )
        }
    }

    @Composable
    private fun NewDatabaseButton() {
        var showDialog by remember { mutableStateOf(false) }

        val navigator = LocalNavigator.currentOrThrow

        Button(onClick = { showDialog = true }) {
            Icon(Icons.Default.Add, "New Database")
        }

        NewDatabaseDialog(
            showDialog, { databaseName, databasePassword ->
                val newDatabase = createDatabase(databaseName, databasePassword)

                navigator.replace(DatabaseScreen(newDatabase))
            }, {
                showDialog = false
            }
        )
    }

    @Composable
    private fun NewDatabaseDialog(
        showDialog: Boolean,
        onConfirm: (databaseName: String, databasePassword: String) -> Unit,
        onCancel: () -> Unit
    ) {
        var databaseName by remember { mutableStateOf("") }
        var databasePassword by remember { mutableStateOf("") }

        if (showDialog) {
            SimpleDialog(
                title = "New Database",
                onConfirm = { onConfirm(databaseName, databasePassword) },
                onCancel = onCancel
            ) {
                Column {
                    OutlinedTextField(
                        value = databaseName,
                        onValueChange = { databaseName = it },
                        label = { Text("Database Name") }
                    )

                    OutlinedTextField(
                        value = databasePassword,
                        onValueChange = { databasePassword = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            }
        }
    }

    @Composable
    private fun OpenDatabaseDialog(
        showDialog: Boolean,
        onConfirm: (databasePassword: String) -> Unit,
        onCancel: () -> Unit
    ) {
        var databasePassword by remember { mutableStateOf("") }

        if (showDialog) {
            SimpleDialog(
                title = "Open Database",
                onConfirm = { onConfirm(databasePassword) },
                onCancel = onCancel
            ) {
                OutlinedTextField(
                    value = databasePassword,
                    onValueChange = { databasePassword = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }
    }

    @Composable
    private fun SimpleDialog(
        title: String,
        onConfirm: () -> Unit,
        onCancel: () -> Unit,
        content: @Composable () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onCancel,
            title = { Text(title) },
            text = { content() },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = onCancel) {
                    Text("Cancel")
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }

    private fun createDatabase(databaseName: String, databasePassword: String): Database {
        val newDatabase = Database(databaseName)

        fileManager.saveDatabase(newDatabase, databasePassword)

        return newDatabase
    }
}