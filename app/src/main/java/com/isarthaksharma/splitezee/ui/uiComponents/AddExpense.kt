package com.isarthaksharma.splitezee.ui.uiComponents

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.localStorage.dataClass.PersonalDataClass
import com.isarthaksharma.splitezee.viewModel.ViewModelPersonalDB
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpense(
    sheetState: SheetState,
    onDismiss:()->Unit,
    viewModelPersonalDB: ViewModelPersonalDB = hiltViewModel()
) {
    val context = LocalContext.current
    val mCalendar = Calendar.getInstance()

    var expenseState by remember { mutableStateOf("") }
    var amountState by remember { mutableStateOf("") }
    var msgState by remember { mutableStateOf("") }
    val selectedDate = remember { mutableLongStateOf(System.currentTimeMillis())  }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply { set(year, month, dayOfMonth) }
            selectedDate.longValue = calendar.timeInMillis
        },
        mCalendar.get(Calendar.YEAR),
        mCalendar.get(Calendar.MONTH),
        mCalendar.get(Calendar.DAY_OF_MONTH)
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            OutlinedTextField(
                value = expenseState,
                onValueChange = { expenseState = it },
                label = { Text("Expense Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
            )

            OutlinedTextField(
                value = amountState,
                onValueChange = { amountState = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = { Icon(Icons.Default.Money, contentDescription = null) }
            )

            OutlinedTextField(
                value = msgState,
                onValueChange = { msgState = it },
                label = { Text("Message (Optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                leadingIcon = { Icon(Icons.AutoMirrored.Filled.NoteAdd, contentDescription = null) }
            )

            Button(
                onClick = {
                    datePickerDialog.datePicker.maxDate = mCalendar.timeInMillis
                    datePickerDialog.show()
                },
                modifier = Modifier.padding(16.dp),
            ) {
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDate.longValue))
                Text(text = formattedDate)
            }

            Button(
                onClick = {
                    if(expenseState.isEmpty()){
                        Toast.makeText(context, "Expanse must have a name", Toast.LENGTH_SHORT).show()
                    }
                    else if (amountState.toDoubleOrNull() == null) {
                        Toast.makeText(context, "Everything has a price", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context,"Added",Toast.LENGTH_LONG).show()
                        val expense = PersonalDataClass(
                            expenseName = expenseState,
                            expenseAmt = amountState.toDoubleOrNull()!!,
                            expenseMsg = msgState,
                            expenseDate = selectedDate.longValue,
                            expenseCurrency = "₹"
                        )
                        viewModelPersonalDB.addPersonalExpense(expense)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 30.dp),
            ) {
                Text("Add Expense")
            }
        }
    }
}
