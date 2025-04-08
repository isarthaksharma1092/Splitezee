package com.isarthaksharma.splitezee.appScreen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import com.isarthaksharma.splitezee.ui.uiComponents.AddGroupExpense
import com.isarthaksharma.splitezee.ui.uiComponents.AddGroupMember
import com.isarthaksharma.splitezee.ui.uiComponents.AnimatedLiquidFAB
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDetail
import com.isarthaksharma.splitezee.viewModel.ViewModelSaveUserInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun GroupDetailsPage(
    viewModelGroupDetail: ViewModelGroupDetail = hiltViewModel(),
    viewModelSaveUserInfo: ViewModelSaveUserInfo = hiltViewModel(),
    groupId: String,
    goToGroupSettings: () -> Unit
) {
    LaunchedEffect(groupId) {
        viewModelGroupDetail.fetchGroupDetails(groupId)
        viewModelGroupDetail.getMembersByGroupId(groupId)
    }

    val gradientColors = if (isSystemInDarkTheme()) {
        listOf(Color(0xFF1A237E), Color(0xFF9575CD), Color(0xFF000000))
    } else {
        listOf(Color(0xFF64B5F6), Color(0xFF9575CD), Color(0xFFFFFFFF))
    }

    val headerCardColor = if (isSystemInDarkTheme()) {
        Color(0xFF2E3B55).copy(alpha = 0.6f)
    } else {
        Color(0xFFE3F2FD).copy(alpha = 0.6f)
    }

    val groupDetailPage by viewModelGroupDetail.groupDetails.collectAsState()
    val context = LocalContext.current
    var isEditSheetOpen by rememberSaveable { mutableStateOf(false) }

    val groupMembers by viewModelGroupDetail.groupMembers.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var addGroupExpense by rememberSaveable { mutableStateOf(false) }
    var addGroupMember by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = gradientColors))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ****************** HEADER ******************
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)),
                colors = CardDefaults.cardColors(containerColor = headerCardColor),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                ) {

                    // ***** Top Bar *****
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Group Details",
                            fontFamily = FontFamily(Font(R.font.nabla_heading)),
                            style = MaterialTheme.typography.displaySmallEmphasized,
                            textAlign = TextAlign.Center,
                        )
                        // Using IconButton for better click handling
                        IconButton(onClick = { goToGroupSettings() }) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ***** Group Name & Creation Date *****
                    Text(
                        groupDetailPage?.groupName ?: "Unavailable",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.headlineLargeEmphasized,
                        fontFamily = FontFamily(Font(R.font.roboto_flex))
                    )

                    Text(
                        "Created on: ${groupDetailPage?.groupCreateDate?.let { formatDate(it) } ?: "Unknown"}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMediumEmphasized,
                        fontFamily = FontFamily(Font(R.font.roboto_flex))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ***** Group Members (Avatars + Names) *****
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            groupMembers.forEach { member ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF64B5F6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (member.userId.isNullOrEmpty()) {
                                            Text(
                                                member.displayName.first().toString(),
                                                color = Color.White,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        } else {
                                            Image(
                                                painter = rememberAsyncImagePainter(member.profileImage),
                                                contentDescription = "Profile image of ${member.displayName}",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                    }
                                    Text(
                                        text = member.displayName.substringBefore(" "),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontSize = 12.sp,
                                    )
                                }
                            }
                            // Add Member Button (Fixed Clickable)
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Member",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF64B5F6))
                                    .padding(10.dp)
                                    .clickable { addGroupMember = true }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ***** Expenses Summary *****
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total Expense: ₹${groupDetailPage?.totalExpense ?: 0.0}",
                            style = MaterialTheme.typography.bodyLargeEmphasized,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onBackground,
                        )

                        Text(
                            "Your Share: ₹${groupDetailPage?.yourShare ?: 0.0}",
                            style = MaterialTheme.typography.bodyLargeEmphasized,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

            }

            Box(modifier = Modifier.padding(bottom = 30.dp)) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painterResource(R.drawable.group_nothing_found),
                        contentDescription = "No Expense Added",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .weight(.1f)
                    )
                }
                AnimatedLiquidFAB(
                    onShareClick = {
                        val share = Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "https://github.com/isarthaksharma1092/Splitezee/releases"
                            )
                            putExtra(Intent.EXTRA_TITLE, "Checkout Splitezee")
                            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        }, null)
                        context.startActivity(share)
                    },
                    onInformationClick = {
                        Toast.makeText(context, "Add Member", Toast.LENGTH_SHORT).show()
                    },
                    onAddExpenseClick = {
                        addGroupExpense = true
                    }
                )

            }
        }
    }
    // ****************** Edit Group Modal ******************
    if (isEditSheetOpen) {
        ModalBottomSheet(onDismissRequest = { isEditSheetOpen = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Edit Group", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = "Trip to Jaipur",
                    onValueChange = {},
                    label = { Text("Group Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { isEditSheetOpen = false }) {
                    Text("Save Changes")
                }
            }
        }
    }

    if (addGroupExpense) {
        AddGroupExpense(
            onDismiss = { addGroupExpense = false },
            groupMembers = groupMembers,
        )
    }

    if (addGroupMember) {
        val member = GroupMemberDataClass (
            groupId = "randomGroupID",
            userId = "savedUser.savedUser_ID ?",
            email = "email",
            displayName = "savedUser.savedUser_Name ?: email.substringBefore",
            profileImage = "savedUser.savedUser_Profile",
            registered = true
        )
        AddGroupMember(

            onDismiss = { addGroupMember = false },
            snackbarHostState = snackbarHostState,
            onAddMember = {}
//                { email ->
//                viewModelGroupDetail.insertGroup(member)
//            }
        )
    }

    SnackbarHost(hostState = snackbarHostState)
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}