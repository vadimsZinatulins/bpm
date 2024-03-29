package com.vadims.application.bpm

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vadims.application.bpm.utils.storage.AndroidFileManager
import com.vadims.application.bpm.utils.ui.CredentialsDecoratorService
import com.vadims.application.bpm.utils.ui.GroupsDecoratorService
import utils.ServiceLocator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ServiceLocator.registerService(AndroidFileManager(this), "FileManager")
        ServiceLocator.registerService(GroupsDecoratorService(), "GroupsDecorator")
        ServiceLocator.registerService(CredentialsDecoratorService(), "CredentialDecorator")

        setContent {
            App()
        }
    }
}
