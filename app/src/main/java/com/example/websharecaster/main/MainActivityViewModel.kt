package com.example.websharecaster.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.websharecaster.api.File
import com.example.websharecaster.api.models.SccData
import com.example.websharecaster.cast.CastComponent
import com.google.android.gms.cast.framework.CastContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivityViewModel(
    private val app: Application,
) : ViewModel(), KoinComponent {
    private val cast: CastComponent by inject()
    val data: MutableLiveData<MutableList<SccData>> = MutableLiveData(mutableListOf())

    fun setCastContext(castContext: CastContext) = cast.setCastContext(castContext)
    fun play(movie: SccData, link: String) = cast.play(movie, link)

}