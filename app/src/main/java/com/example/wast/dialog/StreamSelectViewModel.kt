package com.example.wast.dialog

import androidx.lifecycle.ViewModel
import com.example.wast.api.WebRepository
import com.example.wast.api.models.SccData
import com.example.wast.cast.CastComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StreamSelectViewModel : ViewModel(), KoinComponent {
    private val repo: WebRepository by inject()
    private val cast: CastComponent by inject()

    fun playMedia(parentMedia: SccData? = null, media: SccData, mediaIdentifier: String){
        CoroutineScope(Dispatchers.IO).launch {
            val fileLink = repo.getFileLink(mediaIdentifier)?.body()?.link
            withContext(Dispatchers.Main) {
                if (!fileLink.isNullOrEmpty()) {
                    cast.play(parentMedia, media, fileLink)
                } else {
                    //TODO nepodarilo sa získat link
                }
            }
        }
    }
}