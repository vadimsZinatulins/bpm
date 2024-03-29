package com.vadims.application.bpm.utils.ui

import androidx.compose.runtime.Composable
import data.Group
import utils.ui.IGroupsDecoratorService

class GroupsDecoratorService : IGroupsDecoratorService {
    override fun decorate(groups: List<Group>, onSelectedChanged: () -> Unit): @Composable () -> Unit {
        return {}
    }
}