package com.example.kotlinprogectapp.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.kotlinprogectapp.ui.navigation.Screen
import com.example.kotlinprogectapp.ui.theme.Green
import com.example.kotlinprogectapp.ui.theme.Orange500
import com.example.kotlinprogectapp.ui.theme.Red

@Composable
fun RegisterScreen(navController: NavController) {
    val vm: RegisterViewModel = hiltViewModel()
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
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text("Создать аккаунт", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value         = state.username,
            onValueChange = vm::onUsernameChanged,
            label         = { Text("Имя") },
            singleLine    = true,
            modifier      = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value         = state.tag,
            onValueChange = vm::onTagChanged,
            label         = { Text("@тег") },
            singleLine    = true,
            modifier      = Modifier.fillMaxWidth(),
            prefix        = { Text("@") },
            trailingIcon  = {
                when {
                    state.isCheckingTag  -> CircularProgressIndicator(
                        modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = Orange500
                    )
                    state.tagAvailable == true  -> Icon(Icons.Default.Check, null, tint = Green)
                    state.tagAvailable == false -> Icon(Icons.Default.Close, null, tint = Red)
                }
            },
            supportingText = when (state.tagAvailable) {
                false -> ({ Text("Тег уже занят", color = Red) })
                else  -> null
            }
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value           = state.email,
            onValueChange   = vm::onEmailChanged,
            label           = { Text("Email") },
            singleLine      = true,
            modifier        = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value                = state.password,
            onValueChange        = vm::onPasswordChanged,
            label                = { Text("Пароль") },
            singleLine           = true,
            modifier             = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        if (state.error != null) {
            Spacer(Modifier.height(8.dp))
            Text(state.error!!, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick  = vm::onSubmit,
            enabled  = state.isValid && !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Orange500)
        ) {
            if (state.isLoading) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            else Text("Создать аккаунт", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}
