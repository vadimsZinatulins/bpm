package utils.ui

import androidx.compose.runtime.Composable
import data.Credential

class CredentialDecoratorService : ICredentialDecoratorService {
    override fun decorate(credential: Credential): @Composable () -> Unit {
        return {}
    }
}
