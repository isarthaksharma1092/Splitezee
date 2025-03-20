package com.isarthaksharma.splitezee.ui.uiComponents

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isarthaksharma.splitezee.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@Composable
fun ExpenseShowCard(
    expense: String,
    expenseDate: Long,
    expenseAmt: Double,
    expenseMsg: String?,
    expenseCurrency: String
) {
    val colorInvert:Color = if (isSystemInDarkTheme()) { Color.Black }else{ Color.White }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) {
            Color.White
        }else{
            Color.Black
        } ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Title and Date Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = expense,
                    color = colorInvert,
                    style = MaterialTheme.typography.headlineSmall,
                    fontFamily = FontFamily(Font(R.font.roboto_flex, FontWeight.ExtraBold)),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

                )

                Text(
                    text = convertLongToDate(expenseDate),
                    color = colorInvert,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily(Font(R.font.roboto_flex, FontWeight.Bold)),
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }

            // Expense Amount Section
            Text(
                text = "$expenseCurrency ${String.format("%.2f", expenseAmt)}",
                color = colorInvert,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily(Font(R.font.roboto_flex)),
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            // Expense Message Section
            if (!expenseMsg.isNullOrBlank()) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)),
                )

                Text(
                    text = "Note : $expenseMsg",
                    color = colorInvert,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily(Font(R.font.roboto_flex)),
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                )
            }
        }
    }
}

// Convert Long to Date
fun convertLongToDate(time: Long): String {
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(Date(time))
}