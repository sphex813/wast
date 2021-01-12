package com.example.wast

import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent

class SplashScreenViewModel(private val loginComponent: LoginComponent) : ViewModel(), KoinComponent {

    suspend fun isLoggedIn(): Boolean {
        return loginComponent.hasToken()
    }

}