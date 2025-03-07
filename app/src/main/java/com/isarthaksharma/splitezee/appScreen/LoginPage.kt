package com.isarthaksharma.splitezee.appScreen

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.repository.AuthResponse
import com.isarthaksharma.splitezee.viewModel.AuthenticateUserViewModel

@Composable
fun LoginPage(
    authenticateUserViewModel: AuthenticateUserViewModel = hiltViewModel(),
    goHomePage: () -> Unit
) {
    val authState by authenticateUserViewModel.authState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(authState) {
        if (authState is AuthResponse.Success) {
            goHomePage()
            Toast.makeText(context, "Welcome", Toast.LENGTH_LONG).show()
        }
    }

    when (authState) {
        is AuthResponse.Loading -> {
            CustomProgressBar()
        }
        else -> {
            LoginUI(authenticateUserViewModel)
        }
    }
}

@Composable
fun LoginUI(authenticateUserViewModel: AuthenticateUserViewModel) {
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
                authenticateUserViewModel.signInWithGoogle()
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

@Composable
fun CustomProgressBar() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.onBackground,
            strokeWidth = 6.dp
        )
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
    }
}