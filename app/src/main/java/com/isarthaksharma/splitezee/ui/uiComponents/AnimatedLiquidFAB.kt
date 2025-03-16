package com.isarthaksharma.splitezee.ui.uiComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedLiquidFAB(
    onShareClick: () -> Unit,
    onAddMemberClick: () -> Unit,
    onAddExpenseClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 45f else 0f, label = "FAB Rotation")
    val dismissClick = rememberUpdatedState { isExpanded = false }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = isExpanded, onClick = { isExpanded = false })
            .padding(bottom = 10.dp,end = 10.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AnimatedVisibility(visible = isExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Share", color = MaterialTheme.colorScheme.background)
                    FloatingActionButton(
                        onClick = {
                            onShareClick()
                            dismissClick.value.invoke()
                        },
                        containerColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            }


            AnimatedVisibility(visible = isExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Add Member", color = MaterialTheme.colorScheme.background)
                    FloatingActionButton(
                        onClick = {
                            onAddMemberClick()
                            dismissClick.value.invoke()
                        },
                        containerColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Add Member")
                    }
                }
            }
            AnimatedVisibility(visible = isExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Add Expense", color = MaterialTheme.colorScheme.background)
                    FloatingActionButton(
                        onClick = {
                            onAddExpenseClick()
                            dismissClick.value.invoke()
                        },
                        containerColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        Icon(Icons.Default.RequestPage, contentDescription = "Add Expense")
                    }
                }
            }

            FloatingActionButton(
                onClick = { isExpanded = !isExpanded },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Expand Menu",
                    modifier = Modifier.rotate(rotationAngle)
                )
            }
        }
    }
}
