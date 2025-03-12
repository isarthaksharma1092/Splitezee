package com.isarthaksharma.splitezee.ui.uiComponents

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDataClass
import com.isarthaksharma.splitezee.viewModel.ViewModelFireStoreUpload
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDB
import java.util.regex.Pattern

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroup(
    onDismiss: () -> Unit,
    viewModelGroupDB: ViewModelGroupDB = hiltViewModel(),
    viewModelFireStoreUpload: ViewModelFireStoreUpload = hiltViewModel()
) {
    var isEmailExists by remember { mutableStateOf<Boolean?>(null) }

    var groupName by remember { mutableStateOf("") }
    var memberEmail by remember { mutableStateOf("") }
    val selectedMembers = remember { mutableStateListOf<String>() }
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
    val context = LocalContext.current

    LaunchedEffect(viewModelFireStoreUpload.emailExists.collectAsState().value) {
        isEmailExists = viewModelFireStoreUpload.emailExists.value
        if (isEmailExists == true) {
            val email = memberEmail.trim()
            if (email.isNotEmpty() && email != currentUserEmail && !selectedMembers.contains(email)) {
                selectedMembers.add(email)
                memberEmail = ""
            }
        }
        else Toast.makeText(context, "No User Found !", Toast.LENGTH_SHORT).show()
    }
    LaunchedEffect(Unit) {
        if (!selectedMembers.contains(currentUserEmail)) {
            selectedMembers.add(currentUserEmail)
        }
    }

    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Create Group", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(8.dp))

            // Add Group Name *************************
            TextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Members Text *************************
            Text(
                "Add Members",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Add Members Text field *************************
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = memberEmail,
                    onValueChange = { memberEmail = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Add Member Email") }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        val email = memberEmail.trim()
                        if (isValidEmail(email)) viewModelFireStoreUpload.checkEmail(email)
                        else Toast.makeText(context,"Enter a valid Email Address",Toast.LENGTH_SHORT).show()
                    },
                    enabled = memberEmail.trim().isNotEmpty() && !selectedMembers.contains(memberEmail.trim())
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Member")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Show Added Members *************************
            if (selectedMembers.size > 1) {

                Column {
                    Text("Members Added:", style = MaterialTheme.typography.titleMedium)

                    selectedMembers.forEach { member ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = member, modifier = Modifier.weight(1f))
                            if (member == currentUserEmail) { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") }
                            if (member != currentUserEmail) {
                                IconButton(onClick = { selectedMembers.remove(member) }) {
                                    Icon(Icons.Default.Close, contentDescription = "Remove Member")
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (groupName.isNotEmpty())
                    {
                        if (selectedMembers.size > 1)
                        {
                            val groupDB = GroupDataClass(
                                groupName = groupName,
                                groupAdmin = currentUserEmail,
                                groupMembers = selectedMembers,
                                syncStatus = false,
                                groupCreationData = System.currentTimeMillis()
                            )
                            viewModelGroupDB.createGroup(groupDB)
                            onDismiss()
                        }
                        else Toast.makeText(context, "Add at least one member", Toast.LENGTH_SHORT).show()
                    }
                    else Toast.makeText(context, "You forgot to add Group Name", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = groupName.isNotEmpty() && selectedMembers.size > 1
            ) {
                Text("Create Group")
            }
        }
    }
}

fun isValidEmail(str: String): Boolean {
    val emailPattern = Pattern.compile(
        "[A-Za-z0-9+_.-]+@gmail.com"
    )
    return emailPattern.matcher(str).matches()
}