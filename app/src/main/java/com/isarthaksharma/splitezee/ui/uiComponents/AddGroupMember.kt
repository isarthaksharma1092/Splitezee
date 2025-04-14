package com.isarthaksharma.splitezee.ui.uiComponents

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.SavedUserInfoDataClass
import com.isarthaksharma.splitezee.viewModel.ViewModelFireStore
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDetail
import com.isarthaksharma.splitezee.viewModel.ViewModelSaveUserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupMember(
    viewModelSaveUserInfo: ViewModelSaveUserInfo = hiltViewModel(),
    viewModelGroupDetail: ViewModelGroupDetail = hiltViewModel(),
    viewModelFireStoreUpload: ViewModelFireStore = hiltViewModel(),

    onDismiss: () -> Unit,
    groupId: String,
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
                    val email = emailInput.trim().lowercase()
                    if (email.isNotBlank() &&
                        Pattern.compile("[A-Za-z0-9+_.-]+@gmail.com").matcher(email).matches()
                    ) {
                        viewModelSaveUserInfo.checkIfUserExists(email) { exist ->
                            if (exist) {
                                Log.d("FirestoreDebug", "FireStore fetching avoided ")
                                viewModelSaveUserInfo.getSavedUserInfo(email){ savedUser ->
                                    val member = GroupMemberDataClass(
                                        groupId = groupId,
                                        userId = savedUser.savedUser_ID ?:email,
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
                                        groupId = groupId,
                                        userId = userId ?: email,
                                        email = email,
                                        displayName = name ?: email.substringBefore("@"),
                                        profileImage = profilePic,
                                        registered = userId != null
                                    )
                                    viewModelGroupDetail.insertMember(member)

                                    // Saving into Room:
                                    val saveUserInfo = SavedUserInfoDataClass(
                                        savedUser_ID = userId ?: email,
                                        savedUser_Name = name ?: email.substringBefore("@"),
                                        savedUser_Profile = profilePic,
                                        savedUser_Email = email
                                    )
                                    viewModelSaveUserInfo.insertSavedUserInfo(savedUserInfoDataClass = saveUserInfo)
                                }
                            }
                        }

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

