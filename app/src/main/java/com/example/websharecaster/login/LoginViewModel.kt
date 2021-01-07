package com.example.websharecaster.login

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.websharecaster.api.LoginResponse
import com.example.websharecaster.api.WebApi
import com.example.websharecaster.datastore.LocalStorage
import com.example.websharecaster.datastore.PreferenceKeys
import com.example.websharecaster.utils.HelpUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
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

    suspend fun salt() = webshareApi.salt(userName.value.toString())

    suspend fun saveLoginData(token: String?) = withContext(Dispatchers.IO) {
        val deferreds = listOf(
            async { localStorage.storeValue(PreferenceKeys.USER_NAME, userName.value.toString()) },
            async { localStorage.storeValue(PreferenceKeys.PASSWORD, password.value.toString()) },
            async { localStorage.storeValue(PreferenceKeys.TOKEN, token) }
        )
        deferreds.awaitAll()
    }

    suspend fun hasToken(): Boolean {
        return localStorage.getValue(PreferenceKeys.TOKEN) != null
    }

    suspend fun loginChanged(): Boolean {
        return getFromStorage(PreferenceKeys.USER_NAME) != userName.value.toString() ||
                getFromStorage(PreferenceKeys.PASSWORD) != password.value.toString()

    }

    suspend fun <T> getFromStorage(key: Preferences.Key<T>): T? {
        return localStorage.getValue(key)
    }

}
