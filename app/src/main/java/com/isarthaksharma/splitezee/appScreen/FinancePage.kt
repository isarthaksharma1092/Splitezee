package com.isarthaksharma.splitezee.appScreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.isarthaksharma.splitezee.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FinancePage(modifier: Modifier){
    val context = LocalContext.current
    Column(modifier) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
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
                        onClick = { Toast.makeText(context,"Information Pending",Toast.LENGTH_LONG).show()}
                    )
                )
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight().weight(.1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon( Icons.Outlined.PrivacyTip, tint = MaterialTheme.colorScheme.primary, contentDescription = null)
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