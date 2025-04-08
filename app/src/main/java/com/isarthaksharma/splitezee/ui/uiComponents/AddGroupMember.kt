package com.isarthaksharma.splitezee.ui.uiComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupMember(
    onDismiss: () -> Unit,
    onAddMember: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var emailInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Add a Member",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            TextField(
                value = emailInput,
                onValueChange = {
                    emailInput = it
                    isError = false
                },
                label = { Text("Enter member's email") },
                isError = isError,
                modifier = Modifier.fillMaxWidth()
            )

            if (isError) {
                Text("Please enter a valid email.", color = Color.Red)
            }

            Button(
                onClick = {
                    if (emailInput.isNotBlank() &&
                        android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()
                    ) {

                        isLoading = true

                        // Simulate async add
                        CoroutineScope(Dispatchers.Main).launch {
                            isLoading = false
                            val success = true
                            onDismiss()
                            snackbarHostState.showSnackbar(
                                if (success) "Member added successfully!"
                                else "User not found or failed to add."
                            )
                        }

                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(Icons.Default.PersonAdd, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add Member")
                }
            }
        }
    }
}

//    3. Composable Usage
//            kotlin
//    Copy
//    Edit
//    if (isAddMemberSheetVisible) {
//        AddMemberBottomSheet(
//            onDismiss = { isAddMemberSheetVisible = false },
//            snackbarHostState = snackbarHostState,
//            onAddMember = { email ->
//                viewModelGroupDetail.addMemberToGroup(email, groupId)
//            }
//        )
//    }
//
//    SnackbarHost(hostState = snackbarHostState)
