package com.example.websharecaster

import android.app.Application
import com.example.websharecaster.api.CustomConverter
import com.example.websharecaster.api.WebApi
import com.example.websharecaster.api.WebRepository
import com.example.websharecaster.cast.CastComponent
import com.example.websharecaster.datastore.LocalStorage
import com.example.websharecaster.dialog.StreamSelectViewModel
import com.example.websharecaster.login.LoginViewModel
import com.example.websharecaster.main.MainActivityViewModel
import com.example.websharecaster.menu.MenuViewModel
import com.example.websharecaster.search.SearchViewModel
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
    viewModel { MenuViewModel(get()) }
    viewModel { EpisodesViewModel() }
    viewModel { SeriesViewModel() }
    viewModel { StreamSelectViewModel() }
}
