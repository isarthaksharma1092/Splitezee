package com.isarthaksharma.splitezee.appScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.viewModel.ViewModelGroupDetail

@Composable
fun GroupSettingPage(
    viewModelGroupDetail: ViewModelGroupDetail = hiltViewModel(),
    groupId: String
) {
    LaunchedEffect(groupId) {
        viewModelGroupDetail.fetchGroupDetails(groupId)
        viewModelGroupDetail.getMembersByGroupId(groupId)
    }
    val groupDetailPage by viewModelGroupDetail.groupDetails.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 5.dp, end = 5.dp),
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 15.dp, end = 15.dp),
                    contentAlignment = Alignment.Center,

                    ) {
                    groupDetailPage?.let {
                        Text(
                            text = it.groupName,
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }


        // Delete button at the bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp, horizontal = 15.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 50.dp)
            ) {
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(7.dp))
                groupDetailPage?.let {
                    Text(
                        text = it.groupName,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}
