package com.example.wast.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wast.api.models.SccData
import com.example.wast.cast.CastComponent
import com.example.wast.cast.CastPlayListener
import com.example.wast.datastore.LocalStorage
import com.example.wast.datastore.PreferenceKeys
import com.example.wast.login.LoginComponent
import com.google.android.gms.cast.framework.CastContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivityViewModel(
    private val app: Application,
) : ViewModel(), KoinComponent {
    private val cast: CastComponent by inject()
    private val localStorage: LocalStorage by inject()
    private val loginComponent: LoginComponent by inject()
    val menuDisabled = MutableLiveData(false)
    val watchedHistory: MutableLiveData<MutableList<String>> = MutableLiveData()
    val currentlyPlayingTitle: MutableLiveData<String> = MutableLiveData()
    val currentlyPlayingSubTitle: MutableLiveData<String> = MutableLiveData()
    val currentlyPlayingImageLink: MutableLiveData<String> = MutableLiveData()

    fun setCastContext(castContext: CastContext) = cast.setCastContext(castContext)

    fun disableMenu(disableMenu: Boolean) {
        menuDisabled.value = disableMenu
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            loginComponent.logout()
        }
    }

    fun registerOnCustSuccesfullListener() {
        cast.registerCastSuccessListener(object : CastPlayListener {
            override fun addToWatched() {
                CoroutineScope(Dispatchers.IO).launch {
                    watchedHistory.postValue(localStorage.getValueAsMutableList(PreferenceKeys.WATCHED))
                }
            }

            override fun setPlayedMedia(title: String, subTitle: String, image: String) {
                currentlyPlayingTitle.postValue(title)
                currentlyPlayingSubTitle.postValue(subTitle)
                currentlyPlayingImageLink.postValue(image)
            }

        })
    }
}