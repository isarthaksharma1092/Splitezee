package com.isarthaksharma.splitezee.appScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isarthaksharma.splitezee.R

@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupDetailsPage() {
    val isSyncing = true
    var isFABOpen by rememberSaveable { mutableStateOf(false) }
    // ~~~~~ UI
    Box {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Color.White else Color.Black)
        ) {

            Box(
                modifier = Modifier
                    .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
                    .padding(top = 40.dp, start = 10.dp, end = 10.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                Text(
                    "Details",
                    fontFamily = FontFamily(Font(R.font.doto)),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displaySmallEmphasized,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 25.dp,
                            bottomEnd = 25.dp
                        )
                    ), // only bottom corners rounded
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(if (isSystemInDarkTheme()) Color.Black else Color.White),
            ) {
                Column {
                    Spacer(modifier = Modifier
                        .height(20.dp)
                        .background(Color.Black))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 10.dp)
                    ) {
                        Column {
                            Row {
                                Text(
                                    "Trip to Jaipur",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Start,
                                    style = MaterialTheme.typography.titleLargeEmphasized,
                                    fontFamily = FontFamily(Font(R.font.doto))
                                )
                                Icon(
                                    if (isSyncing) Icons.Default.CloudDone else Icons.Default.CloudUpload,
                                    contentDescription = if (isSyncing) "Data is Synced over the cloud" else "Data is not Synced over the cloud",
                                    tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                                )
                            }
                            Text(
                                "Group created on : 10/20/2025",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Start,
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.titleSmallEmphasized,
                                fontFamily = FontFamily(Font(R.font.doto))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Other content
            Text(
                "Group created on : 10/20/2025",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleSmallEmphasized,
                fontFamily = FontFamily(Font(R.font.doto))
            )
        }

        // ******** Floating Action Button
        FloatingActionButton(
            onClick = { isFABOpen = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Row {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                Text(
                    text = "Add Expense",
                    modifier = Modifier.padding(horizontal = 5.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    if (isFABOpen) {
//        AddExpense(sheetState, onDismiss = { isFABOpen = false })
    }
}
