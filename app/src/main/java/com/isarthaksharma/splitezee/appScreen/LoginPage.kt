@file:Suppress("DEPRECATION")

package com.isarthaksharma.splitezee.appScreen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.repository.AuthResponse
import com.isarthaksharma.splitezee.viewModel.AuthenticateUserViewModel
import com.isarthaksharma.splitezee.viewModel.ViewModelPersonalDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun LoginPage(
    authenticateUserViewModel: AuthenticateUserViewModel = hiltViewModel(),
    viewModelPersonalDB: ViewModelPersonalDB = hiltViewModel(),
    goHomePage: () -> Unit
) {
    val authState by authenticateUserViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        if (authState is AuthResponse.Success) {
            val userId = Firebase.auth.currentUser?.uid ?: return@LaunchedEffect
            viewModelPersonalDB.syncExpensesFromFireStore(userId) // ✅ Sync once only
            goHomePage()
            Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
        }
    }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try { authenticateUserViewModel.signInWithGoogle(task) }
        catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    when (authState) {
        is AuthResponse.Loading -> CustomProgressBar()
        else -> LoginUI(authenticateUserViewModel,launcher)
    }
}

@Composable
fun LoginUI(
    authenticateUserViewModel: AuthenticateUserViewModel,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 55.dp, horizontal = 15.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "App Icon",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .height(250.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val signInIntent = authenticateUserViewModel.getGoogleSignInIntent()
                Log.d("GoogleSignIn", "Launching Google Sign-In Intent")
                launcher.launch(signInIntent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 50.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google Icon",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = "Login with Google")
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CustomProgressBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 55.dp, horizontal = 15.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(350.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground,
                    strokeWidth = 5.dp
                )

                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "App Icon",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 30.dp)
                        .height(250.dp)
                        .clip(CircleShape)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(350.dp)
            ) {
                Text(
                    text = "Please wait while we set this device up for you, this may take a few moments.....",
                    style = MaterialTheme.typography.bodyMediumEmphasized,
                    textAlign = TextAlign.Justify,
                    fontFamily = FontFamily( Font(R.font.doto,FontWeight.Bold))
                )
            }
        }
    }
}