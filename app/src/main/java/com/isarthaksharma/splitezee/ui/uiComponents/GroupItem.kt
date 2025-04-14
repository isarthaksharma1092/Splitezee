package com.isarthaksharma.splitezee.ui.uiComponents

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@SuppressLint("DefaultLocale")
@Composable
fun GroupItem(
    groupID: String,
    groupName: String,
    totalExpense: Double,
    personalBalance: Double,
    groupDetails: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { groupDetails(groupID) }
            .padding(top = 10 .dp, start = 7.dp, end = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar-like circle with gradient
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF42A5F5), Color(0xFF478ED1))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = groupName.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Group info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = groupName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Total: ₹${String.format("%,.2f", totalExpense)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Personal balance (color-coded)
        Text(
            text = if (personalBalance >= 0) "+₹${String.format("%,.2f", personalBalance)}"
            else "-₹${String.format("%,.2f", -personalBalance)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (personalBalance >= 0) Color(0xFF00C853) else Color(0xFFD32F2F)
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .border(BorderStroke(0.5.dp, if(isSystemInDarkTheme())Color.LightGray else Color.DarkGray)),
    )
}
