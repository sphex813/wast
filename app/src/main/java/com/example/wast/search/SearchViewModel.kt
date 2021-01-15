package com.example.wast.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wast.api.WebRepository
import com.example.wast.api.models.SccData
import com.example.wast.api.models.SccResponse
import com.example.wast.api.models.StreamInfo
import com.example.wast.datastore.LocalStorage
import com.example.wast.datastore.PreferenceKeys
import com.example.wast.models.LoadingState
import com.example.wast.utils.Coroutines
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class SearchViewModel(
    private val app: Application,
) : ViewModel(), KoinComponent {
    private val repo: WebRepository by inject()
    private val localStorage: LocalStorage by inject()
    val loading = MutableLiveData<LoadingState>()
    val searchedFileName = MutableLiveData<String?>()
    val showHistory = MutableLiveData<Boolean>()
    val data: MutableLiveData<MutableList<SccData>?> = MutableLiveData()
    val history: MutableLiveData<MutableList<String>?> = MutableLiveData()

    val searchWithDebounce: () -> Unit = Coroutines.debounce(viewModelScope, 300, { search() })

    fun search(historyItem: String? = null) {
        loading.value = LoadingState.LOADING
        addToSearchHistory()
        val valueToSearch = historyItem ?: searchedFileName.value.toString()
        CoroutineScope(Dispatchers.IO).launch {
            repo.search(valueToSearch).let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        loading.value = LoadingState.LOADED
                        data.postValue(it.body()?.data as MutableList<SccData>?)
                    } else {
                        loading.value = LoadingState.error(it.message())
                    }
                }
            }
        }
    }

    suspend fun getStreams(mediaId: String): List<StreamInfo> {
        return repo.streams(mediaId).body()!!
    }

    private fun getData(
        funct: suspend () -> Response<SccResponse>,
    ) {
        loading.value = LoadingState.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            funct().let {
                withContext(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        loading.value = LoadingState.LOADED
                        data.postValue(it.body()?.data as MutableList<SccData>?)
                    } else {
                        loading.value = LoadingState.error(it.message())
                    }
                }
            }
        }
    }

    fun getMovies() {
        getData(repo::newMovies)
    }

    fun getMoviesCzsk() {
        getData(repo::newDubbedMovies)
    }

    fun getTvshows() {
        getData(repo::newShows)
    }

    fun getTvshowsCzsk() {
        getData(repo::newDubbedShows)
    }


    private fun storeHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            localStorage.storeValue(PreferenceKeys.HISTORY, history.value?.joinToString(","))
        }
    }

    fun getSearchHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            val historyString = localStorage.getValue(PreferenceKeys.HISTORY)
            history.postValue(
                if (historyString.isNullOrEmpty()) {
                    mutableListOf()
                } else {
                    historyString.split(",").toMutableList()
                }
            )
        }
    }

    private fun addToSearchHistory() {
        if (!searchedFileName.value?.toString().isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                history.postValue(
                    localStorage.addToList(PreferenceKeys.HISTORY, searchedFileName.value.toString())
                )
            }
        }
    }

    fun deleteFromHistory(historyItem: String) {
        CoroutineScope(Dispatchers.IO).launch {
            history.postValue(
                localStorage.removeFromList(PreferenceKeys.HISTORY, historyItem)
            )
        }
    }
}