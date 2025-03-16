package com.isarthaksharma.splitezee.appScreen

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.ui.uiComponents.AnimatedLiquidFAB

@Preview(showSystemUi = true)
@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun GroupDetailsPage() {
    data class GroupMember(
        val name: String,
        val email: String
    )

    val demoGroupMembers = listOf(
        GroupMember(name = "Sarthak Sharma", email = "sarthak@example.com"),
        GroupMember(name = "Aditi Verma", email = "aditi@example.com"),
        GroupMember(name = "Rohan Mehta", email = "rohan@example.com"),
        GroupMember(name = "Neha Kapoor", email = "neha@example.com"),
        GroupMember(name = "Aman Gupta", email = "aman@example.com")
    )
    val isSyncing = false
    val context = LocalContext.current
    var isEditSheetOpen by rememberSaveable { mutableStateOf(false) }
    val cardColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)

    // ~~~~~ UI
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF4A148C), Color(0xFF880E4F))))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // ******** Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)),
                    colors = CardDefaults.cardColors(containerColor = cardColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .wrapContentHeight()
                    ) {
                        Column {

                            // ****************** Heading
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 50.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = "Back Button",
                                    tint = MaterialTheme.colorScheme.background,
                                )
                                Text(
                                    "Group Details",
                                    fontFamily = FontFamily(Font(R.font.doto)),
                                    color = MaterialTheme.colorScheme.background,
                                    style = MaterialTheme.typography.displaySmallEmphasized,
                                    textAlign = TextAlign.Center,
                                )
                                Icon(
                                    if (isSyncing) Icons.Default.CloudDone else Icons.Default.CloudUpload,
                                    contentDescription = "Sync Status",
                                    tint = if (isSyncing) Color.Green else Color.Red,
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // ****************** Title of the Group
                            Text(
                                "Trip to Jaipur",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.background,
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.headlineMediumEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto))
                            )
                            // ****************** Group Creation
                            Text(
                                "Created on : 10/20/2025",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.background,
                                style = MaterialTheme.typography.labelMediumEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto))
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            //  ****************** List Showing Name and Image
                            FlowRow(
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                demoGroupMembers.forEach { member ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.Gray),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(member.name.first().toString(), color = Color.White)
                                        }
                                        Text(member.name.split(" ")[0], fontSize = 12.sp,color = MaterialTheme.colorScheme.background)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total Expense: ₹ 3,500",
                                    style = MaterialTheme.typography.bodyLargeEmphasized,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.background,
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    "Your Share: ₹ 500",
                                    style = MaterialTheme.typography.bodyLargeEmphasized,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colorScheme.background,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            // Other content
            Box(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {
                Text(
                    "Group created on : 10/20/2025",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.titleSmallEmphasized,
                    fontFamily = FontFamily(Font(R.font.doto))
                )
            }
        }

        // ******** Floating Action Button
        Box(modifier = Modifier.padding(bottom = 30.dp)){
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
    if (isEditSheetOpen) {
        ModalBottomSheet(onDismissRequest = { isEditSheetOpen = false }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Edit Group", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = "Trip to Jaipur",
                    onValueChange = {},
                    label = { Text("Group Name") })
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { isEditSheetOpen = false }) {
                    Text("Save Changes")
                }
            }
        }
    }
}