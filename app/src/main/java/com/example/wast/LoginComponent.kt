package com.example.wast

import android.app.Application
import com.example.wast.api.SaltResponse
import com.example.wast.api.WebApi
import com.example.wast.datastore.LocalStorage
import com.example.wast.datastore.PreferenceKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class LoginComponent : KoinComponent {
    private val app: Application by inject()
    private val webshareApi: WebApi by inject()
    private val localStorage: LocalStorage by inject()

    suspend fun hasToken(): Boolean {
        return !localStorage.getValue(PreferenceKeys.TOKEN).isNullOrEmpty()
    }

    suspend fun logout() {
        localStorage.storeValue(PreferenceKeys.TOKEN, "")
    }

    suspend fun salt(username: String): Response<SaltResponse> = webshareApi.salt(username)

    suspend fun saveLoginData(token: String, username: String, password: String) = withContext(Dispatchers.IO) {
        val deferreds = listOf(
            async { localStorage.storeValue(PreferenceKeys.USER_NAME, username) },
            async { localStorage.storeValue(PreferenceKeys.PASSWORD, password) },
            async { localStorage.storeValue(PreferenceKeys.TOKEN, token) }
        )
        deferreds.awaitAll()
    }


}