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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.ui.uiComponents.CardDesign

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun FinancePage(modifier: Modifier,navController:NavHostController) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val isTablet = configuration.screenWidthDp >= 600

    // Track permission request attempts
    var permissionRequestCount by rememberSaveable { mutableIntStateOf(0) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
        } else {
            // Increment counter when permission is denied
            permissionRequestCount++

            if (permissionRequestCount > 2) {
                openAppSettings(context)
            }
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.READ_SMS)
        }
    }

    Column(modifier) {
        // Heading and Information Section
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Finance",
                    style = MaterialTheme.typography.displaySmallEmphasized,
                    fontFamily = FontFamily(Font(R.font.doto, FontWeight.ExtraBold)),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                Icon(
                    Icons.Outlined.Info,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = {
                            Toast.makeText(context, "Information Pending", Toast.LENGTH_LONG).show()
                        }
                    )
                )
            }
        }

        // LazyVerticalGrid to show the cards
        LazyVerticalGrid(
            columns = if (isTablet) GridCells.Fixed(2) else GridCells.Fixed(1),
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(10) {
                CardDesign(
                    availableBalance = 1234.0,
                    lastUpdated = "02/03/2023",
                    accountNumber = "xxx1234",
                    bankName = "BOB",
                    bankLogo = R.drawable.icon_bob
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .weight(.1f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Outlined.PrivacyTip,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
            Text(
                text = "We do not upload your SMS data anywhere. Your personal information stays on your device and is not shared anywhere.",
                style = MaterialTheme.typography.titleSmallEmphasized,
                fontFamily = FontFamily(Font(R.font.doto, FontWeight.ExtraBold)),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Unspecified,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
        }
    }
}

// open app settings
fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}
