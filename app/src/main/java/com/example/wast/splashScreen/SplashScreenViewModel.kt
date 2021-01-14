package com.example.wast.splashScreen

import androidx.lifecycle.ViewModel
import com.example.wast.login.LoginComponent
import org.koin.core.component.KoinComponent

class SplashScreenViewModel(private val loginComponent: LoginComponent) : ViewModel(), KoinComponent {

    suspend fun isLoggedIn(): Boolean {
        return loginComponent.hasToken()
    }

}