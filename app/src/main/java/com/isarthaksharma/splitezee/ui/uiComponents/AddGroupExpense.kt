package com.isarthaksharma.splitezee.ui.uiComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupExpenseDataClass
import com.isarthaksharma.splitezee.localStorage.dataClass.GroupMemberDataClass
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupExpense
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroupExpense(
    viewModelGroupExpense: ViewModelGroupExpense = hiltViewModel(),
    onDismiss: () -> Unit,
    groupMembers: List<GroupMemberDataClass>,
    groupId :String,
    currentUserName:String
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    val expense by viewModelGroupExpense.groupExpenses.collectAsState()

    // States
    var expenseName by remember { mutableStateOf("") }
    var expenseAmt by remember { mutableStateOf("") }
    var expenseNote by remember { mutableStateOf("") }
    var selectedMembers by remember { mutableStateOf<List<GroupMemberDataClass>>(emptyList()) }
    var paymentDoneBy by remember { mutableStateOf(GroupMemberDataClass()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth(),
        sheetMaxWidth = 600.dp,
        sheetGesturesEnabled = true,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface),
        tonalElevation = 8.dp,
        scrimColor = Color.Black.copy(alpha = 0.4f),
        contentWindowInsets = { WindowInsets.navigationBars }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Add Expense", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

            // *************** Add Expense Name ***************
            TextField(
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("Expense Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // *************** Add Expense Amount ***************
            TextField(
                value = expenseAmt,
                onValueChange = { expenseAmt = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // *************** Add Expense Note (Optional) ***************
            TextField(
                value = expenseNote,
                onValueChange = { expenseNote = it },
                label = { Text("Any Note .... ") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // *************** Bill Payment By ***************
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Bill Paid By",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(.1f)
                )
                SingleSelectDropdown(
                    options = groupMembers,
                    onSelectionChange = { paymentDoneBy = it },
                    selectedOption = paymentDoneBy
                )
            }

            // *************** Split Payment With ***************
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Split Between",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(.1f)
                )
                MultiSelectDropdown(
                    options = groupMembers,
                    selectedOptions = selectedMembers,
                    onSelectionChange = { selectedMembers = it }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // *************** Display Members ***************
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(selectedMembers) { member ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF64B5F6)) ,
                            contentAlignment = Alignment.Center,
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
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                        Text(member.displayName, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 12.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // *************** Splitting Algorithm ***************
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        val addExpense = GroupExpenseDataClass(
                            expenseId = UUID.randomUUID().toString(),
                            groupId = groupId,
                            addedBy = currentUserName,
                            expenseTitle = expenseName,
                            totalAmount = expenseAmt,
                            date = System.currentTimeMillis(),
                            splitAmong = selectedMembers,
                        )
                        viewModelGroupExpense.addExpense(addExpense)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Split Expense")
                }
            }
        }
    }
}

@Composable
fun SingleSelectDropdown(
    options: List<GroupMemberDataClass>,
    selectedOption: GroupMemberDataClass,
    onSelectionChange: (GroupMemberDataClass) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded = !expanded }) {
            // Display the selected member's name or a placeholder
            Text(
                text = selectedOption.displayName
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { member ->
                val isSelected = member == selectedOption
                val iconColor = if (isSelected) Color(0xFF4CAF50) else Color.White

                DropdownMenuItem(
                    text = { Text(member.displayName) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = iconColor
                        )
                    },
                    onClick = {
                        onSelectionChange(member)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun MultiSelectDropdown(
    options: List<GroupMemberDataClass>,
    selectedOptions: List<GroupMemberDataClass>,
    onSelectionChange: (List<GroupMemberDataClass>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expanded = !expanded }) {
            Text("Select Members (${selectedOptions.size})")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { member ->
                val isSelected = member in selectedOptions
                val iconColor = if (isSelected) Color(0xFF4CAF50) else Color.White
                DropdownMenuItem(
                    text = { Text(member.displayName) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = iconColor
                        )
                    },
                    onClick = {
                        val newSelection = if (isSelected) selectedOptions - member
                        else selectedOptions + member
                        onSelectionChange(newSelection)
                    }
                )
            }
        }
    }
}
