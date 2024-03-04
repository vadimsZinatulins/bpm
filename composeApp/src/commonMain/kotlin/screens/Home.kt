package screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import utils.security.BPMCipher

class Home : Screen {
    @Composable
    override fun Content() {
        var password by remember { mutableStateOf("cona") }
        var content by remember { mutableStateOf("") }

        val key = BPMCipher.passwordToKey(password, BPMCipher.generateSalt()).encoded

        val encryptedText = BPMCipher.encrypt(content, key)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(value = password, label = { Text("Password") }, onValueChange = { password = it })
            OutlinedTextField(value = content, label = { Text("Content") }, onValueChange = { content = it })
            Text(encryptedText)
        }
    }
}