package com.isarthaksharma.splitezee

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        enableEdgeToEdge()
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
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NavigationBar(
                        modifier = Modifier.align(Alignment.BottomCenter)

                    ) {
                        bottomItems().forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    navController.navigate(item.label)
                                },
                                icon = {
                                    Icon(
                                        if (index == selectedItem) {
                                            item.selectedIcon
                                        } else {
                                            item.unselectedIcon
                                        },
                                        contentDescription = item.title
                                    )
                                }
                            )
                        }
                    }

                    // Floating Point
                    FloatingActionButton(
                        onClick = { /* Handle FAB click */ },
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Center)
                            .offset(y = (-40).dp)
                            .zIndex(1f)

                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Expense")
                    }
                }
            }
        }) {
        // Setting Background
        val color = if (isSystemInDarkTheme()) Color.Black else Color.White

        NavigationPage(
            modifier = Modifier
                .background(color)
                .fillMaxSize()
                .padding(top = 40.dp, start = 10.dp, end = 10.dp),
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
        BottomDataClass(
            title = "Group",
            label = "GroupPage",
            selectedIcon = Icons.Filled.Groups,
            unselectedIcon = Icons.Outlined.Groups
        )
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
            route = NavigationUtility.SplashScreen,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(100)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(100)
                )
            }

        ) {
            SplashScreen(modifier) {
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
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(100)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    tween(100)
                )
            }
        ) {
            onBottomBarVisibilityChange(false)
            LoginPage {
                navController.navigate(NavigationUtility.HomePage) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        // Home Page Navigation
        composable(
            route = NavigationUtility.HomePage,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(100)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(100)
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
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(100)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(100)
                )
            }
        ) {
            onBottomBarVisibilityChange(false)
            SettingPage(modifier)
        }

        // Group Page
        composable(
            route = NavigationUtility.GroupPage,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(100)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(100)
                )
            }
        ) {
            onBottomBarVisibilityChange(true)
            GroupPage(modifier) {
                navController.navigate(NavigationUtility.SettingPage) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
}
