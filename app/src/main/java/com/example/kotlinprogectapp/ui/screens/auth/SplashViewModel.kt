package com.example.kotlinprogectapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinprogectapp.domain.usecase.auth.IsLoggedInUseCase
import com.example.kotlinprogectapp.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isLoggedIn: IsLoggedInUseCase
) : ViewModel() {

    private val _destination = Channel<String>()
    val destination = _destination.receiveAsFlow()

    init {
        viewModelScope.launch {
            val route = if (isLoggedIn()) Screen.Feed.route else Screen.Login.route
            _destination.send(route)
        }
    }
}