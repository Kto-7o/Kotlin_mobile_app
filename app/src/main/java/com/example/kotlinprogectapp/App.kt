package com.example.kotlinprogectapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Аннотация @HiltAndroidApp запускает генерацию кода Hilt
// Без неё DI не будет работать
@HiltAndroidApp
class App : Application()
