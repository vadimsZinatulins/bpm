package screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import data.Database

class DatabaseScreen(private val database: Database): Screen {
    @Composable
    override fun Content() {
        Text(database.fileName)
    }

}