package com.isarthaksharma.splitezee.appScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.isarthaksharma.splitezee.R
import com.isarthaksharma.splitezee.repository.AuthResponse
import com.isarthaksharma.splitezee.viewModel.AuthenticateUserViewModel
import kotlinx.coroutines.delay

@SuppressLint("ResourceType")
@Composable
fun SplashScreen(
    modifier: Modifier,
    authenticateUserViewModel: AuthenticateUserViewModel = hiltViewModel(),
    doNavigation: (Boolean) -> Unit,
) {

    val authState = authenticateUserViewModel.authState.collectAsState()
    LaunchedEffect(Unit) {
        delay(2500)
        if (authState.value is AuthResponse.Success){
            doNavigation(true)
        }else{
            doNavigation(false)
        }

    }

    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painterResource(R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Inside
        )
    }
}