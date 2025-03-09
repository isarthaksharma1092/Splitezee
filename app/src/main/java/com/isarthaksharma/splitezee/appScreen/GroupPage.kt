package com.isarthaksharma.splitezee.appScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.isarthaksharma.splitezee.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupPage(
    modifier: Modifier
){
    Column(modifier) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Groups",
                    style = MaterialTheme.typography.displaySmallEmphasized,
                    fontFamily = FontFamily(Font(R.font.doto, FontWeight.ExtraBold)),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd // ✅ Ensures FAB is at the bottom-right
        ) {
            FloatingActionButton(
                onClick = {
//                    isGroupSheetOpen = true
                },
                modifier = Modifier.padding(10.dp),
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
    }
}