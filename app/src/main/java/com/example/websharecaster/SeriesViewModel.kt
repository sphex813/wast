package com.example.websharecaster

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.websharecaster.api.WebRepository
import com.example.websharecaster.api.models.SccData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SeriesViewModel : ViewModel(), KoinComponent {
    private val repository: WebRepository by inject()
    val data: MutableLiveData<MutableList<SccData>> = MutableLiveData(mutableListOf())

    fun getSeries(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            data.postValue(repository.getContentFromId(id)
                .body()?.data?.filter { item -> item._source.info_labels.mediatype == "season" } as MutableList<SccData>?)
        }
    }

}