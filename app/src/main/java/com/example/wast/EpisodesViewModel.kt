package com.example.wast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wast.api.WebRepository
import com.example.wast.api.models.SccData
import com.example.wast.api.models.StreamInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class EpisodesViewModel : ViewModel(), KoinComponent {
    private val repository: WebRepository by inject()
    val data: MutableLiveData<MutableList<SccData>> = MutableLiveData(mutableListOf())

    fun getEpisodes(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            data.postValue(repository.getContentFromId(id)
                .body()?.data as MutableList<SccData>?)
            //?.filter { item -> item._source.info_labels.mediatype == "episode" } as MutableList<SccData>?)
        }
    }

    suspend fun getStreams(mediaId: String): List<StreamInfo> {
        return repository.streams(mediaId).body()!!
    }


}