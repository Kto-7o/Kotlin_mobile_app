package com.example.kotlinprogectapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kotlinprogectapp.ui.navigation.Screen
import com.example.kotlinprogectapp.ui.screens.feed.FeedScreen
import com.example.kotlinprogectapp.ui.theme.Orange500

@Composable
fun LoginScreen(navController: NavController) {
    val vm: LoginViewModel = hiltViewModel()
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        vm.events.collect { route ->
            navController.navigate(route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Goal", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = Orange500)
        Spacer(Modifier.height(8.dp))
        Text("Войти в аккаунт", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))

        // Поле логина
        OutlinedTextField(
            value = state.login,
            onValueChange = vm::onLoginChanged,
            label = { Text("Логин") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = vm::onPasswordChanged,
            label = { Text("Пароль") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (state.passwordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon  = {
                IconButton(onClick = vm::onPasswordVisibilityToggle) {
                    Icon(
                        imageVector  = if (state.passwordVisible)
                            Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Показать пароль"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )

        if (state.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text  = state.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick  = vm::onSubmit,
            enabled  = !state.isLoading && state.login.isNotBlank() && state.password.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Orange500)
        ) {
            if (state.isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            else Text("Войти", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(12.dp))

        TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
            Text("Нет аккаунта? Зарегистрироваться")
        }
    }
}


