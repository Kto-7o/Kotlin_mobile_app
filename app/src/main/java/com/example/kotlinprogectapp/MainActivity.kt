package com.example.kotlinprogectapp


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kotlinprogectapp.ui.navigation.AppNavGraph
import com.example.kotlinprogectapp.ui.navigation.MainScreen
import com.example.kotlinprogectapp.ui.screens.auth.LoginScreen
import com.example.kotlinprogectapp.ui.screens.auth.RegisterScreen
import com.example.kotlinprogectapp.ui.screens.feed.FeedScreen
import com.example.kotlinprogectapp.ui.theme.KotlinProgectAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinProgectAppTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
                //MainScreen(rootNavController = navController)
            }
        }
    }
}

