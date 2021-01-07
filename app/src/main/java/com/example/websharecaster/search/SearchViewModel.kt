package com.example.websharecaster.search

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

class SearchViewModel(
    private val app: Application,
) : ViewModel(), KoinComponent {
    private val api: WebApi by inject()
    private val repo: WebRepository by inject()
    private val localStorage: LocalStorage by inject()
    private val cast: CastComponent by inject()
    val loading = MutableLiveData<LoadingState>()
    val searchedFileName = MutableLiveData<String?>()
    val data: MutableLiveData<MutableList<SccData>?> = MutableLiveData()

    val searchWithDebounce: () -> Unit = Coroutines.debounce(viewModelScope, 300, { search() })

    fun search() {
        loading.value = LoadingState.LOADING
        CoroutineScope(Dispatchers.IO).launch {
            repo.search(searchedFileName.value.toString()).let {
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
}