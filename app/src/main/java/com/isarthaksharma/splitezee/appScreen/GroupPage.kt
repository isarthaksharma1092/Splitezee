package com.isarthaksharma.splitezee.appScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.ui.uiComponents.CreateGroup
import com.isarthaksharma.splitezee.ui.uiComponents.GroupItem
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDB

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupPage(
    modifier: Modifier,
    viewModelGroupDB: ViewModelGroupDB = hiltViewModel(),
    groupDetailsPage: (String) -> Unit
) {
    var isGroupSheetOpen by rememberSaveable { mutableStateOf(false) }
    val groupDB by viewModelGroupDB.group.collectAsState()

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // ***************** Header *****************
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Groups",
                    style = MaterialTheme.typography.displaySmallEmphasized,
                    fontFamily = FontFamily(Font(R.font.nabla_heading, FontWeight.ExtraBold)),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }

            // ***************** Group List *****************
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(groupDB) {it ->
                    GroupItem(
                        groupID = it.groupId,
                        groupName = it.groupName,
                        groupMembers = it.groupMembers,
                        totalExpense = 0.0,
                        personalBalance = 0.0
                    ) {groupId ->
                        groupDetailsPage(groupId)
                    }
                }
            }
        }

        // ***************** Floating Action Button (FAB) *****************
        FloatingActionButton(
            onClick = { isGroupSheetOpen = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row {
                Icon(
                    Icons.Default.GroupAdd,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                Text(
                    text = "Create Group",
                    modifier = Modifier.padding(horizontal = 5.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    // ***************** Create Group Bottom Sheet *****************
    if (isGroupSheetOpen) {
        CreateGroup(onDismiss = { isGroupSheetOpen = false })
    }
}
