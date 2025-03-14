package com.isarthaksharma.splitezee.appScreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.ui.uiComponents.LogoutAlertBox
import com.isarthaksharma.splitezee.viewModel.AuthenticateUserViewModel
import com.isarthaksharma.splitezee.viewModel.UserInfoViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingPage(
    userInfoViewModel: UserInfoViewModel = hiltViewModel(),
    authenticateUserViewModel: AuthenticateUserViewModel = hiltViewModel(),
    goLoginScreen: () -> Unit
) {
    val userProfile by userInfoViewModel.userProfile.collectAsState()
    var openAlertDialog by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf("₹ - Indian Rupee") }
    val context = LocalContext.current

    Column(modifier = Modifier
        .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
        .padding(top = 40.dp, start = 10.dp, end = 10.dp)){
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.displaySmallEmphasized,
                fontFamily = FontFamily(Font(R.font.doto, FontWeight.ExtraBold)),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(10.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    Column {
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
                    }
                    Spacer(modifier = Modifier.weight(.1f))
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        painter = if (userProfile?.userProfilePictureUrl == "null") painterResource(
                            R.drawable.man_icon
                        )
                        else rememberAsyncImagePainter(userProfile?.userProfilePictureUrl),
                        contentDescription = "${userProfile?.userName}'s Picture",
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
                .padding(10.dp)
        ) {
            // Sync Toggle
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Text(text = "Sync Data Across Devices")
                Spacer(modifier = Modifier.weight(1f))
                var isSyncEnabled by remember { mutableStateOf(true) }
                Switch(
                    checked = isSyncEnabled,
                    onCheckedChange = { isSyncEnabled = it }
                )
            }

            // Select Default Currency
            Row(verticalAlignment = Alignment.CenterVertically)
            {
                Text(text = "Select the Currency ")
                Spacer(modifier = Modifier.weight(1f))
                DropdownMenuButton(
                    selectedValue = selectedCurrency,
                    options = currencies,
                    onSelect = {
                        selectedCurrency = it
                        Toast.makeText(
                            context,
                            "$selectedCurrency is your default currency from now on",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { openAlertDialog = true },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.onErrorContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 50.dp)
                    .padding(vertical = 20.dp)

            ) {
                Image(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout Button",
                    modifier = Modifier.size(30.dp),
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Logout",
                    color = MaterialTheme.colorScheme.background,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        if (openAlertDialog) {
            LogoutAlertBox(
                onDismissRequest = { openAlertDialog = false },
                onConfirmation = {
                    openAlertDialog = false
                    authenticateUserViewModel.logout()
                    goLoginScreen()
                },
                dialogTitle = "Logout",
                dialogText = "All your saved data will be removed. Are you sure you want to proceed ?"
            )
        }
    }
}

// Select Default Currency

@Composable
fun DropdownMenuButton(
    selectedValue: String,
    options: List<String>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.clip(RoundedCornerShape(10.dp))) {
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

// List of top 30 currencies
val currencies = listOf(
    "$ - United States Dollar",
    "€ - Euro",
    "¥ - Japanese Yen",
    "£ - British Pound Sterling",
    "A$ - Australian Dollar",
    "C$ - Canadian Dollar",
    "Fr - Swiss Franc",
    "¥ - Chinese Yuan",
    "₹ - Indian Rupee",
    "$ - Mexican Peso",
    "R$ - Brazilian Real",
    "₽ - Russian Ruble",
    "$ - Hong Kong Dollar",
    "kr - Swedish Krona",
    "$ - New Zealand Dollar",
    "$ - Singapore Dollar",
    "kr - Norwegian Krone",
    "₩ - South Korean Won",
    "₺ - Turkish Lira",
    "R - South African Rand",
    "ر.س - Saudi Riyal",
    "RM - Malaysian Ringgit",
    "Rp - Indonesian Rupiah",
    "฿ - Thai Baht",
    "د.إ - UAE Dirham",
    "zł - Polish Zloty",
    "$ - Colombian Peso",
    "₪ - Israeli Shekel",
    "$ - Chilean Peso",
    "₱ - Philippine Peso"
)
