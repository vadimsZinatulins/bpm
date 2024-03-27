package utils.ui

import androidx.compose.runtime.Composable
import data.Group

interface IGroupsDecoratorService {
    fun decorate(groups: List<Group>, onSelectedChanged: () -> Unit): @Composable () -> Unit
}