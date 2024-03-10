package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import utils.storage.FileManager
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import components.GenericDialog

class DatabasesListScreen(private val fileManager: FileManager) : Screen {
    @Composable
    override fun Content() {
        var files by remember { mutableStateOf(fileManager.listFiles()) }
        var fileToDelete by remember { mutableStateOf("") }

        val deleteFile = {
            fileManager.deleteFile(fileToDelete)
            fileToDelete = ""
            files = fileManager.listFiles()
        }

        val cancelFileDeletion = { fileToDelete = "" }

        Column {
            NewDatabase { fileName, password ->
                fileManager.saveFile(fileName, password)
                files = fileManager.listFiles()
            }

            if (files.isEmpty()) {
                Text("No databases found", modifier = Modifier.fillMaxWidth(), fontSize = 32.sp, textAlign = TextAlign.Center)
            }

            Spacer(modifier = Modifier.size(28.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(files) { file ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(file.name.split(".").first(), fontSize = 24.sp, modifier = Modifier.align(Alignment.CenterVertically))
                        Icon(
                            Icons.Default.Delete,
                            "Delete ${file.name}",
                            modifier = Modifier.align(Alignment.CenterVertically).clickable {
                                fileToDelete = file.name
                            },
                            tint = Color.Red
                        )
                    }
                }
            }
            DeleteDatabaseDialog(fileToDelete, deleteFile, cancelFileDeletion)
        }
    }

    @Composable
    private fun NewDatabase(onNewFileAdded: (fileName: String, password: String) -> Unit) {
        var isNewDatabaseDialogOpen by remember { mutableStateOf(false) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Button(onClick = { isNewDatabaseDialogOpen = true }) {
                Text("New Database")
            }
        }

        if(isNewDatabaseDialogOpen) {
            NewDatabaseDialog({ isNewDatabaseDialogOpen = false }, { fileName, password ->
                onNewFileAdded(fileName, password)
                isNewDatabaseDialogOpen = false
            })
        }
    }

    @Composable
    private fun DeleteDatabaseDialog(filename: String, onConfirm: () -> Unit = {}, onCancel: () -> Unit = {}) {
        if (filename.isNotEmpty()) {
            GenericDialog(
                title = "Delete $filename?",
                onCancel = onCancel,
                onConfirm = onConfirm,
                onDismiss = onCancel,
            ) {
                Text("Are you sure you want to delete $filename?")
            }
        }
    }

    @Composable
    private fun NewDatabaseDialog(onCancel: () -> Unit, onAccept: (fileName: String, password: String) -> Unit) {
        var fileName by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordConfirmation by remember { mutableStateOf("") }

        val isFilenameValid = fileName.isNotEmpty() && !fileName.contains("[\\s/\\\\?*:|\"<>]+".toRegex())
        val isPasswordValid = password.isNotEmpty() && password == passwordConfirmation

        GenericDialog(
            title = "New Database",
            onDismiss = onCancel
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedTextField(value = fileName, onValueChange = { fileName = it }, label = { Text("Database Name") }, isError = fileName.isNotEmpty() && fileName.contains("[\\s/\\\\?*:|\"<>]+".toRegex()), singleLine = true)
                PasswordField("Password", password) { password = it }
                PasswordField("Password Confirmation", passwordConfirmation, isError = passwordConfirmation.isNotEmpty() && passwordConfirmation != password) { passwordConfirmation = it }
                Spacer(modifier = Modifier.size(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    Button(onClick = { onCancel() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(onClick = { onAccept(fileName, password) }, enabled = passwordConfirmation.isNotEmpty() && isFilenameValid && isPasswordValid) {
                        Text("Create")
                    }
                }
            }
        }
    }

    @Composable
    private fun PasswordField(label: String, value: String, isError: Boolean = false, onChange : (String) -> Unit = {}) {
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            label = { Text(label) },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = isError,
            trailingIcon = {
                val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val description = if (isPasswordVisible) "Hide password" else "Show password"
                val tint = if (isPasswordVisible) Color.DarkGray else Color.LightGray
                Icon(icon, description, tint = tint, modifier = Modifier.clickable { isPasswordVisible = !isPasswordVisible })
            }
        )
    }
}