package com.example.kotlinprogectapp.ui.navigation

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

sealed class Screen(val route: String) {
    object Splash        : Screen("splash")
    object Login         : Screen("login")
    object Register      : Screen("register")
    object Feed          : Screen("feed")
    object Create        : Screen("create")
    object Friends       : Screen("friends")
    object Profile       : Screen("profile")
    object FriendProfile : Screen("profile/{userId}") {
        fun createRoute(userId: Long) = "profile/$userId"
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController    = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Feed.route) {
            FeedScreen(navController)
        }
        composable(Screen.Create.route) {
            CreateChallengeScreen(navController)
        }
        composable(Screen.Friends.route) {
            FriendsScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController, userId = null)
        }
        composable(
            route     = Screen.FriendProfile.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId")
            ProfileScreen(navController, userId = userId)
        }
    }
}