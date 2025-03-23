package com.isarthaksharma.splitezee.appScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import com.isarthaksharma.splitezee.ui.uiComponents.AddExpense
import com.isarthaksharma.splitezee.ui.uiComponents.AlertBoxMenu
import com.isarthaksharma.splitezee.ui.uiComponents.EditExpense
import com.isarthaksharma.splitezee.ui.uiComponents.ExpenseShowCard
import com.isarthaksharma.splitezee.viewModel.UserInfoViewModel
import com.isarthaksharma.splitezee.viewModel.ViewModelFireStore
import com.isarthaksharma.splitezee.viewModel.ViewModelPersonalDB

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier,
    userInfoViewModel: UserInfoViewModel = hiltViewModel(),
    viewModelPersonalDB: ViewModelPersonalDB = hiltViewModel(),
    viewModelFireStore: ViewModelFireStore = hiltViewModel(),
    goSetting: () -> Unit
) {
    val userProfile by userInfoViewModel.userProfile.collectAsState()
    val expenses by viewModelPersonalDB.expenses.collectAsState()

    val todayExpense by viewModelPersonalDB.personalTodayExpense.collectAsState()
    val monthExpense by viewModelPersonalDB.personalMonthExpense.collectAsState()
    val totalExpense by viewModelPersonalDB.personalAllExpense.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var isPersonalSheetOpen by rememberSaveable { mutableStateOf(false) }

    val cardColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
    var openDelAlert by remember { mutableStateOf(false) }
    var selectedExpense by remember { mutableStateOf<PersonalDataClass?>(null) }
    var openEditDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ***************** Greeting + Profile Picture *****************
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hi ${(userProfile?.userName)?.substringBefore(" ")},",
                    style = MaterialTheme.typography.displaySmallEmphasized,
                    fontFamily = FontFamily(Font(R.font.nabla_heading, FontWeight.Bold)),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = { goSetting() }, enabled = true),
                    painter = if (userProfile?.userProfilePictureUrl == "null") painterResource(
                        R.drawable.man_icon
                    ) else rememberAsyncImagePainter(userProfile?.userProfilePictureUrl),
                    contentDescription = "${userProfile?.userName}'s Picture",
                )
            }

            // ***************** Total Spent Banner *****************
            Card(
                elevation = CardDefaults.cardElevation(100.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 10.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        // ~~ Total Spending Column
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Total Spent",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineSmallEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                            Text(
                                text = "₹ ${totalExpense ?: 0}",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineMediumEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                        }

                        // ~~ Vertical Divider
                        VerticalDivider(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(2.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        // ~~ Today & Monthly Expense Column
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Today",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineSmallEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                            Text(
                                text = "₹ ${todayExpense ?: 0}",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineMediumEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Monthly",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineSmallEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                            Text(
                                text = "₹ ${monthExpense ?: 0}",
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.headlineMediumEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto)),
                            )
                        }
                    }
                }
            }

            // ~~ Expense List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(expenses) { expense ->

                    val swipeState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            when (it) {
                                SwipeToDismissBoxValue.EndToStart -> {
                                    selectedExpense = expense
                                    openDelAlert = true
                                    false
                                }

                                SwipeToDismissBoxValue.StartToEnd -> {
                                    selectedExpense = expense
                                    openEditDialog = true
                                    false
                                }

                                else -> false
                            }
                        },
                        positionalThreshold = { 0.25f }
                    )

                    // Delete Confirmation Dialog
                    if (openDelAlert && selectedExpense == expense) {
                        AlertBoxMenu(
                            onDismissRequest = {
                                openDelAlert = false
                                selectedExpense = null
                            },
                            onConfirmation = {
                                selectedExpense?.let {
                                    viewModelPersonalDB.removePersonalExpense(it)
                                    viewModelFireStore.removePersonalExpense(expenseID = it.expenseId)
                                }
                                openDelAlert = false
                                selectedExpense = null
                            },
                            dialogTitle = "Delete Expense?",
                            dialogText = "Are you sure you want to delete it?",
                            option1 = "Cancel",
                            option2 = "Delete"
                        )
                    }

                    // Edit Expense Dialog
                    if (openEditDialog && selectedExpense == expense) {
                        openEditDialog = true
                    }
                    val backgroundColor by animateColorAsState(
                        targetValue = when (swipeState.targetValue) {
                            SwipeToDismissBoxValue.StartToEnd -> Color.Blue // Edit
                            SwipeToDismissBoxValue.EndToStart -> Color.Red // Delete
                            else -> Color.Gray // Default (neutral state)
                        }, label = "SwipeColor"
                    )
                    SwipeToDismissBox(
                        state = swipeState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .background(backgroundColor) // Smooth color transition
                                    .padding(horizontal = 8.dp),
                                contentAlignment = if (swipeState.targetValue == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = if (swipeState.targetValue == SwipeToDismissBoxValue.StartToEnd) Icons.Default.Edit else Icons.Default.Delete,
                                    contentDescription = if (swipeState.targetValue == SwipeToDismissBoxValue.StartToEnd) "Edit" else "Delete",
                                    tint = Color.White
                                )
                            }
                        },
                        enableDismissFromStartToEnd = true,
                        enableDismissFromEndToStart = true,
                        gesturesEnabled = true,
                        content = {
                            ExpenseShowCard(
                                expense.expenseName,
                                expense.expenseDate,
                                expense.expenseAmt,
                                expense.expenseMsg,
                                expense.expenseCurrency
                            )
                        }
                    )
                }
            }
        }

        // ~~ Floating Action Button (FAB)
        FloatingActionButton(
            onClick = { isPersonalSheetOpen = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 5.dp).align(Alignment.CenterVertically)
                )
                Text(
                    text = "Add Expense",
                    modifier = Modifier.padding(horizontal = 5.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

    }

    // **Add Expense Bottom Sheet**
    if (isPersonalSheetOpen) {
        AddExpense(sheetState, onDismiss = { isPersonalSheetOpen = false })
    }

    if(openEditDialog){
        EditExpense(
            expense = selectedExpense!!,
            onDismiss = { openEditDialog = false },
            onSave = {updatedExpense->
                openEditDialog = false
                viewModelFireStore.updatePersonalExpense(updatedExpense)
                viewModelPersonalDB.updatePersonalExpense(updatedExpense)
            }
        )
    }
}
