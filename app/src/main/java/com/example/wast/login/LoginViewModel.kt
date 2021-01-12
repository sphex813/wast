package com.example.wast.login

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wast.LoginComponent
import com.example.wast.api.LoginResponse
import com.example.wast.api.SaltResponse
import com.example.wast.api.WebApi
import com.example.wast.datastore.LocalStorage
import com.example.wast.utils.HelpUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response


class LoginViewModel(
    private val app: Application,
) : ViewModel(), KoinComponent {
    var userName = MutableLiveData<String>("")
    var password = MutableLiveData<String>("")
    var isLoginValid = MutableLiveData<Boolean>(true)
    private val webshareApi: WebApi by inject()
    private val localStorage: LocalStorage by inject()
    private val loginComponent: LoginComponent by inject()


    suspend fun login(salt: String): Response<LoginResponse>? {
        val hashedPassword = HelpUtils.getHashedPassword(password.value.toString(), salt)
        if (!hashedPassword.isNullOrEmpty()) {
            return webshareApi.login(
                userName.value.toString(),
                hashedPassword,
                1
            )
        }
        return null
    }

    suspend fun salt(): Response<SaltResponse> {
        return loginComponent.salt(userName.value.toString())
    }

    suspend fun saveLoginData(token: String) {
        loginComponent.saveLoginData(token, userName.value.toString(), password.value.toString())
    }

    suspend fun <T> getFromStorage(key: Preferences.Key<T>): T? {
        return localStorage.getValue(key)
    }


}
