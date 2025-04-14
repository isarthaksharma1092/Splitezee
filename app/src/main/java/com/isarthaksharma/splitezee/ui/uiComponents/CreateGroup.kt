package com.isarthaksharma.splitezee.ui.uiComponents

import android.annotation.SuppressLint
import android.util.Log
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
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupDetailDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.SavedUserInfoDataClass
import com.isarthaksharma.splitezee.viewModel.ViewModelFireStore
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDB
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDetail
import com.isarthaksharma.splitezee.viewModel.ViewModelSaveUserInfo
import java.util.UUID
import java.util.regex.Pattern

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroup(
    onDismiss: () -> Unit,
    viewModelGroupDB: ViewModelGroupDB = hiltViewModel(),
    viewModelGroupDetail: ViewModelGroupDetail = hiltViewModel(),
    viewModelFireStoreUpload: ViewModelFireStore = hiltViewModel(),
    viewModelSaveUserInfo: ViewModelSaveUserInfo = hiltViewModel()
) {
    var groupName by remember { mutableStateOf("") }
    var memberEmail by remember { mutableStateOf("") }
    val selectedMembers = remember { mutableStateListOf<String>() }
    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
    val context = LocalContext.current

    // ~~ UI
    ModalBottomSheet(onDismissRequest = { onDismiss() }) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp))

        {
            Text("Create Group", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(8.dp))

            // ***************** Add Group Name *****************
            TextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ***************** Add Members Text *****************
            Text(
                "Add Members",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            // ***************** Add Members Text field *****************
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

                // ***************** Add Members *****************
                Button(
                    onClick = {
                        val email = memberEmail.trim().lowercase()
                        if (isValidEmail(email)) {
                            if (!selectedMembers.contains(email)) {
                                selectedMembers.add(email)
                                memberEmail = ""
                            } else { Toast.makeText(context, "Email already added!", Toast.LENGTH_SHORT).show() }
                        } else {
                            Toast.makeText(context, "Enter a valid Email Address", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = memberEmail.trim().isNotEmpty()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Member")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ***************** Show Added Members *****************
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
                            if (member == currentUserEmail) {
                                IconButton(onClick = { }) {
                                    Icon(
                                        Icons.Default.AdminPanelSettings,
                                        contentDescription = "Admin"
                                    )
                                }
                            }

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

            // ***************** Create Group Button *****************
// ********************************** ROOM DATABASE & FIREBASE **********************************
// Save group & group details immediately

            Button(
                onClick = {
                    if (groupName.isNotEmpty()) {
                        if (selectedMembers.size > 1) {
                            val randomGroupID = UUID.randomUUID().toString()

                            // Save Group Info into Room
                            val groupDB = GroupDataClass(
                                groupId = randomGroupID,
                                groupName = groupName,
                                groupAdmin = currentUserEmail,
                                syncStatus = false,
                                groupCreationData = System.currentTimeMillis(),
                            )
                            viewModelGroupDB.createGroup(groupDB)


                            val groupDetailDB = GroupDetailDataClass(
                                groupDetailID = randomGroupID,
                                groupName = groupName,
                                groupAdmin = currentUserEmail,
                                groupCreateDate = System.currentTimeMillis(),
                                totalExpense = 0.0,
                                yourShare = 0.0,
                            )
                            viewModelGroupDetail.insertGroup(groupDetailDB)


                            selectedMembers.forEach { email ->
                                Log.d("FirestoreDebug", "Function Called: $email")
                                viewModelSaveUserInfo.checkIfUserExists(email) { exist ->
                                    if (exist) {
                                        Log.d("FirestoreDebug", "FireStore fetching avoided ")
                                        viewModelSaveUserInfo.getSavedUserInfo(email){ savedUser ->
                                            val member = GroupMemberDataClass(
                                                groupId = randomGroupID,
                                                userId = savedUser.savedUser_ID ?: email,
                                                email = email,
                                                displayName = savedUser.savedUser_Name ?: email.substringBefore("@"),
                                                profileImage = savedUser.savedUser_Profile,
                                                registered = true
                                            )
                                            viewModelGroupDetail.insertMember(member)
                                        }
                                    }
                                    else {
                                        Log.d("FirestoreDebug", "FireStore fetching Starts ...")
                                        viewModelFireStoreUpload.fetchUserInfoByEmail(email) { name, profilePic, userId ->
                                            val member = GroupMemberDataClass(
                                                groupId = randomGroupID,
                                                userId = userId ?: email,
                                                email = email,
                                                displayName = name ?: email.substringBefore("@"),
                                                profileImage = profilePic,
                                                registered = userId != null
                                            )
                                            viewModelGroupDetail.insertMember(member)

                                            // Saving into Room:
                                            val saveUserInfo = SavedUserInfoDataClass(
                                                savedUser_ID = userId,
                                                savedUser_Name = name,
                                                savedUser_Profile = profilePic,
                                                savedUser_Email = email
                                            )
                                            viewModelSaveUserInfo.insertSavedUserInfo(savedUserInfoDataClass = saveUserInfo)
                                        }
                                    }
                                }
                            }
                            onDismiss()
                        } else { Toast.makeText(context, "Add at least one member", Toast.LENGTH_SHORT).show() }
                    } else {Toast.makeText(context, "You forgot to add Group Name", Toast.LENGTH_SHORT).show() }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = groupName.isNotEmpty() && selectedMembers.size > 1
            ) {
                Text("Create Group")
            }
        }
    }
}
// ***************** Email Pattern *****************
fun isValidEmail(str: String): Boolean {
    val emailPattern = Pattern.compile("[A-Za-z0-9+_.-]+@gmail.com")
    return emailPattern.matcher(str).matches()
}