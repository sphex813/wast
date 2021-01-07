package com.example.websharecaster.dialog

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.websharecaster.models.LoadingState
import com.example.websharecaster.api.WebApi
import com.example.websharecaster.api.WebRepository
import com.example.websharecaster.api.models.SccData
import com.example.websharecaster.api.models.SccResponse
import com.example.websharecaster.api.models.StreamInfo
import com.example.websharecaster.cast.CastComponent
import com.example.websharecaster.datastore.LocalStorage
import com.example.websharecaster.utils.Coroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class StreamSelectViewModel : ViewModel(), KoinComponent {
    private val repo: WebRepository by inject()
    private val cast: CastComponent by inject()

    fun playMedia(media: SccData, mediaIdentifier: String){
        CoroutineScope(Dispatchers.IO).launch {
            val fileLink = repo.getFileLink(mediaIdentifier)?.body()?.link
            withContext(Dispatchers.Main) {
                if (!fileLink.isNullOrEmpty()) {
                    cast.play(media, fileLink)
                } else {
                    //TODO nepodarilo sa získat link
                }
            }
        }
    }
}