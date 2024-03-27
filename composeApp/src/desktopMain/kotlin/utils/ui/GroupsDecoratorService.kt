package utils.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import data.Group
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import screens.DatabaseScreen

class GroupsDecoratorService : IGroupsDecoratorService {
    override fun decorate(groups: List<Group>, onSelectedChanged: () -> Unit): @Composable () -> Unit {
        return {
            LazyColumn(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
            ) {
                items(groups) { group ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    val event = awaitPointerEvent()

                                    if(event.type == PointerEventType.Press) {
                                    }
                                }
                            }
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(color = Color(0.3f, 0.5f, 0.8f))
                            ) {  },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = group.name,
                            tint = Color(0.5f, 0.8f, 1.0f),
                            modifier = Modifier.size(40.dp).clickable {  }
                        )
                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(group.name, fontSize = 28.sp)
                            Text(group.credentials.size.toString(), fontSize = 12.sp)
                        }
                    }

                    /*
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Storage,
                            contentDescription = group.name,
                            tint = Color(0.5f, 0.8f, 1.0f),
                            modifier = Modifier.size(40.dp)
                        )
                        Icon(Icons.Filled.Folder, contentDescription = group.name, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.padding(8.dp))
                        Text(group.name)
                    }
                    */
                }
            }
        }
    }
}