package com.example.wast

import android.app.Application
import com.example.wast.api.CustomConverter
import com.example.wast.api.WebApi
import com.example.wast.api.WebRepository
import com.example.wast.cast.CastComponent
import com.example.wast.datastore.LocalStorage
import com.example.wast.dialog.StreamSelectViewModel
import com.example.wast.login.LoginViewModel
import com.example.wast.main.MainActivityViewModel
import com.example.wast.home.HomeViewModel
import com.example.wast.search.SearchViewModel
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

fun getModules(app: Application) = module {
    single {
        OkHttpClient.Builder()
            .cache(Cache(app.externalCacheDir ?: app.cacheDir, 20000))

            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl(WebApi.WEBSHARE_URL)
            .addConverterFactory(CustomConverter())
            .build()
            .create(WebApi::class.java)
    }

    single { CastComponent() }
    single { LocalStorage() }
    single { WebRepository() }

    viewModel { MainActivityViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { EpisodesViewModel() }
    viewModel { SeriesViewModel() }
    viewModel { StreamSelectViewModel() }
}
