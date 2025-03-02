package com.isarthaksharma.splitezee.appScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.isarthaksharma.splitezee.viewModel.UserInfoViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingPage(
    modifier: Modifier,
    userInfoViewModel: UserInfoViewModel = hiltViewModel()
) {
    val userProfile by userInfoViewModel.userProfile.collectAsState()

    Column(modifier) {
        Text(
            text = "Settings",
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            style = MaterialTheme.typography.displaySmallEmphasized,
            fontFamily = FontFamily(Font(R.font.doto, FontWeight.ExtraBold)),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Name: ${userProfile?.userName}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email (Read-Only)
        Text(
            text = "Email: ${userProfile?.userEmail}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sync Toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Sync Data Across Devices")
            Spacer(modifier = Modifier.weight(1f))
            var isSyncEnabled by remember { mutableStateOf(true) }
            Switch(
                checked = isSyncEnabled,
                onCheckedChange = { isSyncEnabled = it }
            )
        }
    }
}

@Composable
fun DropdownMenuButton(
    selectedValue: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = { expanded = true }) {
            Text(text = selectedValue)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onSelect(option)
                    expanded = false
                })
            }
        }
    }
}
