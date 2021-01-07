package com.example.websharecaster.api

import com.example.websharecaster.api.models.SccResponse
import com.example.websharecaster.api.models.StreamInfo
import com.example.websharecaster.datastore.LocalStorage
import com.example.websharecaster.datastore.PreferenceKeys
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Response

class WebRepository : KoinComponent {
    private val webApi: WebApi by inject()
    private val localStorage: LocalStorage by inject()
//    fun getSalt(userName: String): Models.SaltResponse {
//        return webshareApi.salt(userName).execute
//    }

    suspend fun getFileLink(ident: String): Response<FileLinkResponse>? {
        val token = localStorage.getValue(PreferenceKeys.TOKEN)
        if (token != null) {
            return webApi.fileLink(ident, token)
        }
        return null
    }

    suspend fun newDubbedMovies(): Response<SccResponse> {
        return webApi.newsDubbed(type = "movie")
    }

    suspend fun newDubbedShows(): Response<SccResponse> {
        return webApi.newsDubbed(type = "tvshow")
    }

    suspend fun newMovies(): Response<SccResponse> {
        return webApi.news(type = "movie")
    }

    suspend fun newShows(): Response<SccResponse> {
        return webApi.news(type = "tvshow")
    }

    suspend fun allMovies(): Response<SccResponse> {
        return webApi.all(type = "movie")
    }

    suspend fun allShows(): Response<SccResponse> {
        return webApi.all(type = "tvshow")
    }

    suspend fun search(value: String): Response<SccResponse> {
        return webApi.searchScc(value = value)
    }

    suspend fun getContentFromId(id: String): Response<SccResponse> {
        return webApi.getContentFromId(value = id)
    }

    suspend fun streams(id: String): Response<List<StreamInfo>> {
        return webApi.streams(WebApi.SCC_URL + id + "/streams")
    }
}