package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun GenericDialog(
    title: String,
    onCancel: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    val hasAnyButton = onConfirm != null || onCancel != null

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 24.dp,
            modifier = Modifier
                .wrapContentSize()
                .requiredSizeIn(minWidth = 300.dp, minHeight = 200.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(title, style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.size(16.dp))

                content()

                Spacer(modifier = Modifier.size(16.dp))

                if(hasAnyButton) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    ) {
                        onCancel?.let {
                            Button(onClick = onCancel) {
                                Text("Cancel")
                            }
                        }

                        onConfirm?.let {
                            Button(onClick = onConfirm) {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }
        }
    }
}