package utils.ui

import androidx.compose.runtime.Composable
import data.Credential

interface ICredentialDecoratorService {
    fun decorate(credential: Credential): @Composable () -> Unit
}