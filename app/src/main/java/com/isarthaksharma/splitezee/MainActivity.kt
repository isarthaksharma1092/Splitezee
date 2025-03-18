package com.isarthaksharma.splitezee

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.isarthaksharma.splitezee.appScreen.FinancePage
import com.isarthaksharma.splitezee.appScreen.GroupDetailsPage
import com.isarthaksharma.splitezee.appScreen.GroupPage
import com.isarthaksharma.splitezee.appScreen.HomePage
import com.isarthaksharma.splitezee.appScreen.LoginPage
import com.isarthaksharma.splitezee.appScreen.SettingPage
import com.isarthaksharma.splitezee.appScreen.SplashScreen
import com.isarthaksharma.splitezee.dataClass.BottomDataClass
import com.isarthaksharma.splitezee.ui.theme.SplitezeeTheme
import com.isarthaksharma.splitezee.utlility.NavigationUtility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            SplitezeeTheme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }
    var showBottomBar by rememberSaveable { mutableStateOf(false) }
    var selectedBottomNavBar by rememberSaveable { mutableStateOf("") }

    val gradientColors = listOf(
        Color(0xFF38DF57),
        Color(0xFF04A9FB),
        Color.Black,
        Color.Black,
        Color.Black
    )


    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.2f),  // Light frosted effect
                                    Color.White.copy(alpha = 0.05f) // More transparency at bottom
                                )
                            )
                        )
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(24.dp)) // Subtle border effect
                ) {
                    selectedBottomNavBar = "Personal"
                    NavigationBar(
                        containerColor = Color.Transparent, // Removes Material default color
                        tonalElevation = 0.dp // Removes shadow effects from MaterialTheme
                    ) {
                        bottomItems().forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItem == index,
                                onClick = {
                                    if (selectedItem != index) {
                                        selectedItem = index
                                        selectedBottomNavBar = item.title
                                        navController.navigate(item.label) {
                                            popUpTo(0) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                icon = {
                                    Icon(
                                        if (index == selectedItem) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title,
                                        tint = if (selectedItem == index) Color.Black else Color.Gray
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (selectedItem == index) {
                                            if (isSystemInDarkTheme()) Color.Black else Color.White
                                        } else {
                                            Color.Gray
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { _ ->

        NavigationPage(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = gradientColors))
                .padding(top = 40.dp, start = 10.dp, end = 10.dp, bottom = 100.dp),
            navController
        ) { isBottomBarVisible ->
            showBottomBar = isBottomBarVisible
        }
        SetStatusBarColor()
    }
}

// Setting the Status Bar for transparent Background
@Composable
fun SetStatusBarColor() {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars
        }
    }
}

// Bottom Navigation Items
fun bottomItems(): List<BottomDataClass> {
    val item = listOf(
        BottomDataClass(
            title = "Personal",
            label = "HomePage",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        ),
//        BottomDataClass(
//            title = "Group",
//            label = "GroupPage",
//            selectedIcon = Icons.Filled.Groups,
//            unselectedIcon = Icons.Outlined.Groups
//        ),
        BottomDataClass(
            title = "Finance",
            label = "FinancePage",
            selectedIcon = Icons.Filled.CreditCard,
            unselectedIcon = Icons.Outlined.CreditCard
        ),
    )
    return item
}

// Navigation Controller
@Composable
fun NavigationPage(
    modifier: Modifier,
    navController: NavHostController,
    onBottomBarVisibilityChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationUtility.SplashScreen
    ) {
        // Splash Screen Navigation
        composable(
            route = NavigationUtility.SplashScreen
        ) {
            SplashScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
                    .padding(top = 40.dp, start = 10.dp, end = 10.dp)
            ) {

                if (it) {
                    navController.navigate(NavigationUtility.HomePage) {
                        popUpTo(NavigationUtility.SplashScreen) { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    navController.navigate(NavigationUtility.LoginPage) {
                        popUpTo(NavigationUtility.SplashScreen) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }

        // Login Page Navigation
        composable(
            route = NavigationUtility.LoginPage,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) {
            onBottomBarVisibilityChange(false)
            LoginPage {
                navController.navigate(NavigationUtility.HomePage) {

                    popUpTo(NavigationUtility.HomePage) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        // Home Page Navigation
        composable(
            route = NavigationUtility.HomePage,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) {
            onBottomBarVisibilityChange(true)
            HomePage(modifier) {
                navController.navigate(NavigationUtility.SettingPage)
            }
        }

        //Setting Page
        composable(
            route = NavigationUtility.SettingPage,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) {
            onBottomBarVisibilityChange(false)
            SettingPage {
                navController.navigate(NavigationUtility.LoginPage) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        // Group Page
        composable(
            route = NavigationUtility.GroupPage,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) {
            onBottomBarVisibilityChange(true)
            GroupPage(
                navController,
                modifier,
            ){
                navController.navigate(NavigationUtility.GroupDetailsPage)
            }
        }

        // Finance Page
        composable(
            route = NavigationUtility.FinancePage,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) {
            onBottomBarVisibilityChange(true)
            FinancePage(modifier = modifier)
        }

        composable(
            route = NavigationUtility.GroupDetailsPage,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ){
            onBottomBarVisibilityChange(false)
            GroupDetailsPage()
        }
    }
}
