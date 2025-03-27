package com.isarthaksharma.splitezee.appScreen

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.ui.uiComponents.AnimatedLiquidFAB
import com.isarthaksharma.splitezee.ui.uiComponents.ExpenseShowCard
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDetail
import com.isarthaksharma.splitezee.viewModel.ViewModelPersonalDB

@OptIn(
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

@Composable
fun GroupDetailsPage(
    groupId: String,
    viewModelGroupDetail: ViewModelGroupDetail = hiltViewModel(),
    viewModelPersonalDB: ViewModelPersonalDB = hiltViewModel()
) {
    val gradientColors = if (isSystemInDarkTheme()) {
        listOf(
            Color(0xFF1A237E), // Deep Blue
            Color(0xFF9575CD), // Soft Purple
            Color(0xFF000000)  // Black for depth
        )
    } else {
        listOf(
            Color(0xFF64B5F6), // Light Blue
            Color(0xFF9575CD), // Soft Purple
            Color(0xFFFFFFFF)  // White for clarity
        )
    }
    // Updated Header Card Color
    val headerCardColor = if (isSystemInDarkTheme()) {
        Color(0xFF2E3B55).copy(alpha = 0.6f)
    } else {
        Color(0xFFE3F2FD).copy(alpha = 0.6f)
    }

    val groupDetailPage by viewModelGroupDetail.getGroupDetails(groupId).collectAsState(initial = null)
    val expenses by viewModelPersonalDB.expenses.collectAsState()

    val isSyncing = false
    val context = LocalContext.current
    var isEditSheetOpen by rememberSaveable { mutableStateOf(false) }

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
                colors = CardDefaults.cardColors(containerColor = headerCardColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp,start = 10.dp,end = 10.dp, bottom = 10.dp)
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
                        Icon(
                            if (isSyncing) Icons.Default.CloudDone else Icons.Default.CloudUpload,
                            contentDescription = "Sync Status",
                            tint = if (isSyncing) Color.Green else Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ***** Group Name & Creation Date *****
                    Text(
                        ".Trip to Jaipur",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.headlineMediumEmphasized,
                        fontFamily = FontFamily(Font(R.font.doto))
                    )

                    Text(
                        "Created on : 10/20/2025",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMediumEmphasized,
                        fontFamily = FontFamily(Font(R.font.doto))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // ***** Group Members (Avatars + Names) *****
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("Sarthak", "Aditi", "Rohan", "Neha", "Aman").forEach { memberName ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF64B5F6)), // Light Blue Avatar
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        memberName.first().toString(),
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                Text(
                                    text = memberName,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ***** Expenses Summary *****
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total Expense: ₹ 3,500",
                            style = MaterialTheme.typography.bodyLargeEmphasized,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            "Your Share: ₹ 500",
                            style = MaterialTheme.typography.bodyLargeEmphasized,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ****************** EXPENSE LIST ******************
            // Temporary [Testing Purpose]
            LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(expenses) { expense ->
                    ExpenseShowCard(
                        expense.expenseName,
                        expense.expenseDate,
                        expense.expenseAmt,
                        expense.expenseMsg,
                        expense.expenseCurrency
                    )
                }
            }
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
