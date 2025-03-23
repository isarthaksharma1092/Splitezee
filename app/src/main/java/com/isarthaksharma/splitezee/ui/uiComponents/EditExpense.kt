package com.isarthaksharma.splitezee.ui.uiComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpense(
    expense: PersonalDataClass,
    onDismiss: () -> Unit,
    onSave: (PersonalDataClass) -> Unit
) {
    var name by remember { mutableStateOf(expense.expenseName) }
    var amount by remember { mutableStateOf(expense.expenseAmt.toString()) }
    var message by remember { mutableStateOf(expense.expenseMsg) }
    var selectedDate by remember { mutableLongStateOf(expense.expenseDate) }

    val context = LocalContext.current
    val mCalendar = Calendar.getInstance()


    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            selectedDate = calendar.timeInMillis
        },
        mCalendar.get(Calendar.YEAR),
        mCalendar.get(Calendar.MONTH),
        mCalendar.get(Calendar.DAY_OF_MONTH)
    )


    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Edit Expense",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily(Font(R.font.lumpbump))
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Expense Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount", fontFamily = FontFamily(Font(R.font.lumpbump))) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = if(message.toString().isEmpty()){"Optional"} else{message.toString()},
                onValueChange = { message = it },
                label = { Text("Description", fontFamily = FontFamily(Font(R.font.lumpbump))) },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    datePickerDialog.datePicker.maxDate = mCalendar.timeInMillis
                    datePickerDialog.show()
                },
                modifier = Modifier.padding(16.dp),
            ) {
                val formattedDate = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                ).format(Date(selectedDate))
                Text(text = formattedDate, fontFamily = FontFamily(Font(R.font.lumpbump)))
            }

            Button(
                onClick = {
                    val updatedExpense = expense.copy(
                        expenseName = name,
                        expenseAmt = amount.toDoubleOrNull() ?: expense.expenseAmt,
                        expenseMsg = message,
                        expenseDate = selectedDate
                    )
                    onSave(updatedExpense)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 30.dp)
            ) {
                Text("Save", fontFamily = FontFamily(Font(R.font.lumpbump)))
            }
        }
    }
}
