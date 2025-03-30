package com.isarthaksharma.splitezee.appScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.ui.uiComponents.AnimatedLiquidFAB
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDetail

@OptIn(ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun GroupDetailsPage(
    viewModelGroupDetail: ViewModelGroupDetail = hiltViewModel(),
    groupId: String,
    goToGroupSettings: () -> Unit
) {
    LaunchedEffect(groupId) {
        viewModelGroupDetail.fetchGroupDetails(groupId)
        viewModelGroupDetail.getMembersByGroupId(groupId)
    }

    val gradientColors = if (isSystemInDarkTheme()) { listOf(Color(0xFF1A237E), Color(0xFF9575CD), Color(0xFF000000)) }
    else { listOf(Color(0xFF64B5F6), Color(0xFF9575CD), Color(0xFFFFFFFF))
    }

    val headerCardColor = if (isSystemInDarkTheme()) { Color(0xFF2E3B55).copy(alpha = 0.6f) }
    else { Color(0xFFE3F2FD).copy(alpha = 0.6f) }

    val groupDetailPage by viewModelGroupDetail.groupDetails.collectAsState()
    val context = LocalContext.current
    var isEditSheetOpen by rememberSaveable { mutableStateOf(false) }

    val groupMembers by viewModelGroupDetail.groupMembers.collectAsState()

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
                        IconButton(onClick = {
                            Toast.makeText(context, "Settings Clicked", Toast.LENGTH_SHORT).show()
                            goToGroupSettings()
                        }) {
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            groupMembers.forEach { member ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF64B5F6)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            member.displayName.first().toString(),
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    Text(
                                        text = member.displayName,
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
                                    .clickable { goToGroupSettings() }
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
            // ******** Floating Action Button
            Box(modifier = Modifier.padding(bottom = 30.dp)) {
                AnimatedLiquidFAB(
                    onShareClick = { Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show() },
                    onAddMemberClick = {
                        Toast.makeText(context, "Add Member", Toast.LENGTH_SHORT).show()
                    },
                    onAddExpenseClick = {
                        Toast.makeText(context, "Add Expense", Toast.LENGTH_SHORT).show()
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
}

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}