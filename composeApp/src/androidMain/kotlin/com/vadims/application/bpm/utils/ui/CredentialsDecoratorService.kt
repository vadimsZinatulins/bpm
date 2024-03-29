package com.vadims.application.bpm.utils.ui

import data.Credential
import utils.ui.ICredentialDecoratorService

class CredentialsDecoratorService : ICredentialDecoratorService {
    override fun decorate(credential: Credential): () -> Unit {
        return {}
    }
}