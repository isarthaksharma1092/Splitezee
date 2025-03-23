package com.isarthaksharma.splitezee.ui.uiComponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

// LogoutAlertDialog is a alert dialog to warn user before logging out.
@Composable
fun AlertBoxMenu(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    option1:String,
    option2:String
) {
    AlertDialog(
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(option2, color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(option1, color = MaterialTheme.colorScheme.onBackground)
            }
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
}
