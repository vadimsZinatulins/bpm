package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import utils.storage.FileManager
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HdrPlus
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DatabasesListScreen(private val fileManager: FileManager) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Database.updateDatabases(fileManager)

        var databases by remember { mutableStateOf(Database.databases) }
        var databaseToDelete by remember { mutableStateOf<Database?>(null) }
        var databaseToOpen by remember { mutableStateOf<Database?>(null) }

        val deleteDatabase: () -> Unit = {
            databaseToDelete?.let { it: Database ->
                fileManager.deleteFile(it.fileName)
                databaseToDelete = null
                Database.updateDatabases(fileManager)
                databases = Database.databases
            }
        }

        val openDatabase: () -> Unit = {
            databaseToOpen?.let { it: Database ->
                navigator.replace(DatabaseScreen(it))
            }
        }

        val cancelDatabaseDeletion = { databaseToDelete = null }
        val cancelDatabaseOpening = { databaseToOpen = null }

        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f),

        ) {
            if (databases.isEmpty()) {
                Text(
                    "No databases found",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(16.dp).weight(1f)
            ) {
                items(databases) { database ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(color = Color(0.3f, 0.5f, 0.8f))
                            ) { if (database.isOpen) navigator.replace(DatabaseScreen(database)) else databaseToOpen = database }
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Storage,
                            "New Database",
                            tint = Color(0.5f, 0.8f, 1.0f),
                            modifier = Modifier.size(40.dp)
                        )
                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                database.fileName.split(".").first(),
                                fontSize = 28.sp
                            )
                            Text(
                                "Last modified on 12/12/2021 at 12:35",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            NewDatabase { fileName, password ->
                val newDatabase = Database.newDatabase(fileName, password, fileManager)

                navigator.replace(DatabaseScreen(newDatabase))
            }
        }
        OpenDatabaseDialog(
            database = databaseToOpen,
            onCancel = cancelDatabaseOpening,
            onConfirm = openDatabase
        )
        DeleteDatabaseDialog(
            database = databaseToDelete,
            onConfirm = deleteDatabase,
            onCancel = cancelDatabaseDeletion
        )
    }

    @Composable
    private fun NewDatabase(onNewFileAdded: (fileName: String, password: String) -> Unit) {
        var isNewDatabaseDialogOpen by remember { mutableStateOf(false) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Icon(
                Icons.Default.Add,
                "New Database",
                tint = Color(0xFF7AB0D0),
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, CircleShape)
                    .clickable { isNewDatabaseDialogOpen = true }
            )
        }

        if (isNewDatabaseDialogOpen) {
            NewDatabaseDialog({ isNewDatabaseDialogOpen = false }, { fileName, password ->
                onNewFileAdded(fileName, password)
                isNewDatabaseDialogOpen = false
            })
        }
    }

    @Composable
    private fun OpenDatabaseDialog(
        database: Database?,
        onCancel: () -> Unit,
        onConfirm: () -> Unit = {}
    ) {
        var password by remember { mutableStateOf("") }
        var isPasswordValid by remember { mutableStateOf(true) }
        var isLoading by remember { mutableStateOf(false) }

        database?.let { db: Database ->
            val onConfirmAction = if (!isLoading) {
                {
                    val onSuccess: () -> Unit = { MainScope().launch { onConfirm() } }
                    val onError: () -> Unit = { MainScope().launch {
                        isLoading = false
                        isPasswordValid = false
                    }}

                    db.open(password, onSuccess, onError)
                }
            } else null

            val onCancelAction = if (!isLoading) {
                onCancel
            } else null

            GenericDialog(
                title = "Open ${db.fileName}?",
                onCancel = onCancelAction,
                onConfirm = onConfirmAction,
                onDismiss = { },
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                } else {
                    PasswordField(
                        "Password",
                        password,
                        onChange = { password = it },
                        isError = !isPasswordValid
                    )
                }
            }
        }
    }

    @Composable
    private fun DeleteDatabaseDialog(
        database: Database?,
        onConfirm: () -> Unit = {},
        onCancel: () -> Unit = {}
    ) {
        database?.let { db: Database ->
            GenericDialog(
                title = "Delete ${db.fileName}?",
                onCancel = onCancel,
                onConfirm = onConfirm,
                onDismiss = onCancel,
            ) {
                Text("Are you sure you want to delete ${db.fileName}?")
            }
        }
    }

    @Composable
    private fun NewDatabaseDialog(
        onCancel: () -> Unit,
        onAccept: (fileName: String, password: String) -> Unit
    ) {
        var fileName by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordConfirmation by remember { mutableStateOf("") }

        val isFilenameValid =
            fileName.isNotEmpty() && !fileName.contains("[\\s/\\\\?*:|\"<>]+".toRegex())
        val isPasswordValid = password.isNotEmpty() && password == passwordConfirmation

        GenericDialog(
            title = "New Database",
            onDismiss = onCancel
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Database Name") },
                    isError = fileName.isNotEmpty() && fileName.contains("[\\s/\\\\?*:|\"<>]+".toRegex()),
                    singleLine = true
                )
                PasswordField("Password", password) { password = it }
                PasswordField(
                    "Password Confirmation",
                    passwordConfirmation,
                    isError = passwordConfirmation.isNotEmpty() && passwordConfirmation != password
                ) { passwordConfirmation = it }
                Spacer(modifier = Modifier.size(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    Button(onClick = { onCancel() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        onClick = { onAccept(fileName, password) },
                        enabled = passwordConfirmation.isNotEmpty() && isFilenameValid && isPasswordValid
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }

    @Composable
    private fun PasswordField(
        label: String,
        value: String,
        isError: Boolean = false,
        onChange: (String) -> Unit = {}
    ) {
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(label) },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = isError,
            trailingIcon = {
                val icon =
                    if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (isPasswordVisible) "Hide password" else "Show password"
                val tint = if (isPasswordVisible) Color.DarkGray else Color.LightGray
                Icon(
                    icon,
                    description,
                    tint = tint,
                    modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible })
            }
        )
    }
}