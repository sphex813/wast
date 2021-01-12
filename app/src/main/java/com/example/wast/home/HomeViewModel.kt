package com.example.wast.home

import android.app.Application
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent

class HomeViewModel(
    private val app: Application,
) : ViewModel(), KoinComponent {

}