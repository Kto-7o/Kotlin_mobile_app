package com.example.kotlinprogectapp.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kotlinprogectapp.ui.screens.auth.*
import com.example.kotlinprogectapp.ui.screens.create.CreateChallengeScreen
import com.example.kotlinprogectapp.ui.screens.feed.FeedScreen
import com.example.kotlinprogectapp.ui.screens.friends.FriendsScreen
import com.example.kotlinprogectapp.ui.screens.profile.ProfileScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Feed : Screen("feed")
    object Create : Screen("create")
    object Friends : Screen("friends")
    object Profile : Screen("profile")
    object FriendProfile : Screen("profile/{userId}") {
        fun createRoute(userId: Long) = "profile/$userId"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route)   { SplashScreen(navController) }
        composable(Screen.Login.route)    { LoginScreen(navController) }
        composable(Screen.Register.route) { RegisterScreen(navController) }

        // Передаём rootNavController в MainScreen
        composable(Screen.Feed.route) {
            MainScreen(rootNavController = navController)
        }

        composable(
            route     = Screen.FriendProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStack ->
            ProfileScreen(
                rootNavController = navController,
                userId = backStack.arguments?.getLong("userId")
            )
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(rootNavController: NavHostController) {
    // Внутренний контроллер только для переключения вкладок
    val innerNavController = rememberNavController()

    val navItems = listOf(
        Triple(Screen.Feed,    "Лента",   Icons.Default.Home),
        Triple(Screen.Create,  "Создать", Icons.Default.Add),
        Triple(Screen.Friends, "Друзья",  Icons.Default.Group),
        Triple(Screen.Profile, "Профиль", Icons.Default.Person),
    )

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1A1A1A)) {
                val currentRoute = innerNavController
                    .currentBackStackEntryAsState().value
                    ?.destination?.route

                navItems.forEach { (screen, label, icon) ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick  = {
                            innerNavController.navigate(screen.route) {
                                popUpTo(Screen.Feed.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon  = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = Color(0xFFF97316),
                            selectedTextColor   = Color(0xFFF97316),
                            indicatorColor      = Color(0x1AF97316),
                            unselectedIconColor = Color(0xFF888888),
                            unselectedTextColor = Color(0xFF888888)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(innerNavController, startDestination = Screen.Feed.route) {
                composable(Screen.Feed.route) {
                    FeedScreen(innerNavController)
                }
                composable(Screen.Create.route) {
                    CreateChallengeScreen(innerNavController)
                }
                composable(Screen.Friends.route) {
                    FriendsScreen(
                        navController = innerNavController,
                        rootNavController  = rootNavController
                    )
                }
                composable(Screen.Profile.route) {
                    // Передаём rootNavController чтобы выход работал
                    ProfileScreen(rootNavController = rootNavController)
                }
            }
        }
    }
}