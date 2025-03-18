package com.isarthaksharma.splitezee.ui.uiComponents

import com.isarthaksharma.splitezee.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CardDesign(
    availableBalance: String,
    lastUpdated: String,
    accountNumber: String,
    bankName: String,
    bankLogo: Int
) {
    var isVisible by remember { mutableStateOf(false) }

    Box{
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .background(Color.Black)
                .height(190.dp)
                .fillMaxWidth()
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = bankName,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Acc: $accountNumber",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Image(
                    painter = painterResource(R.drawable.wireless_card),
                    contentDescription = "Bank Logo",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Balance Visibility Toggle Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = !isVisible,
                ) {
                    Text(
                        text = "**********",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                AnimatedVisibility(
                    visible = isVisible,
                ) {
                    Text(
                        text = "Rs. $availableBalance",
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isVisible) "Hide Balance" else "Show Balance",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { isVisible = !isVisible }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Last Updated Time
            Text(
                text = "Last Updated: $lastUpdated",
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
