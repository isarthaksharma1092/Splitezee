package com.isarthaksharma.splitezee.appScreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.ui.uiComponents.CardDesign
import com.isarthaksharma.splitezee.ui.uiComponents.convertLongToDate
import com.isarthaksharma.splitezee.viewModel.ViewModelSMS

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun FinancePage(
    viewModelSMS: ViewModelSMS = hiltViewModel(),
    modifier: Modifier
) {
    val colorInvert:Color = if (isSystemInDarkTheme()) { Color.White }else{ Color.Black }

    val smsList by viewModelSMS.smsData.collectAsState()
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val isTablet = configuration.screenWidthDp >= 600

    // ~~ Track permission attempts
    var permissionRequestCount by rememberSaveable { mutableIntStateOf(0) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
            viewModelSMS.smsData
        } else {
            permissionRequestCount++

            // ~~ If denied more than twice, open settings
            if (permissionRequestCount > 2) {
                openAppSettings(context)
            } else {
                showPermissionDialog = true
            }
        }
    }

    // ~~ Corrected Permission Handling
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_SMS)
        } else {
            viewModelSMS.smsData
        }
    }

    Column(modifier) {
        // ~~ Show UI only when permission is granted
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Box {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Finance",
                        style = MaterialTheme.typography.displaySmallEmphasized,
                        fontFamily = FontFamily(Font(R.font.nabla_heading, FontWeight.ExtraBold)),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Icon(
                        Icons.Outlined.Info,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = null,
                        modifier = Modifier.clickable(
                            enabled = true,
                            onClick = {
                                Toast.makeText(context, "Bank Of Baroda Only Supported Bank Right Now As of Now", Toast.LENGTH_LONG).show()
                            }
                        )
                    )
                }
            }

            LazyVerticalGrid(
                columns = if (isTablet) GridCells.Fixed(2) else GridCells.Fixed(1),
                modifier = Modifier.weight(.1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(smsList) { sms ->
                    if (sms.totalBalance != "N/A" || sms.availableBalance != "N/A") {
                        CardDesign(
                            availableBalance = if (sms.totalBalance != "N/A") sms.totalBalance else sms.availableBalance,
                            lastUpdated = convertLongToDate(sms.lastUpdated),
                            accountNumber = sms.accountNumber,
                            bankName = sms.bankName,
                            bankLogo = R.drawable.icon_bob
                        )
                    }
                }
            }
        } else {
            // ~~ Show a message if permission is denied
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "We need SMS permission to fetch financial data securely.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                androidx.compose.material3.Button(
                    onClick = { requestPermissionLauncher.launch(Manifest.permission.READ_SMS) },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Grant Permission")
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Icon(
                Icons.Outlined.PrivacyTip,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 5.dp).align(Alignment.CenterVertically)
            )
            Text(
                text = "Your personal information stays on your device and is not shared anywhere.",
                style = MaterialTheme.typography.titleSmallEmphasized,
                fontFamily = FontFamily(Font(R.font.doto, FontWeight.Bold)),
                color = colorInvert,
            )
        }
    }

    // ~~ Permission Explanation Dialog
    if (showPermissionDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showPermissionDialog = false },
            title = { Text("SMS Permission Needed") },
            text = { Text("We need SMS access to read bank messages securely. Your data is never uploaded.") },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = {
                    showPermissionDialog = false
                    requestPermissionLauncher.launch(Manifest.permission.READ_SMS)
                }) {
                    Text("Try Again")
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showPermissionDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ~~ Function to Open App Settings if Permission is Denied
fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}
