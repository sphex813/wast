package com.example.wast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wast.api.models.SccData
import com.example.wast.cast.CastComponent
import com.google.android.gms.cast.framework.media.RemoteMediaClient.ProgressListener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayViewModel : ViewModel(), KoinComponent {
    var seekbarTouched = false
    private val cast: CastComponent by inject()
    val isPlaying: MutableLiveData<Boolean> = MutableLiveData(true);
    val mediaLength: MutableLiveData<Long> = MutableLiveData()
    val currentPosition: MutableLiveData<Int> = MutableLiveData(0)
    val background: MutableLiveData<String> = MutableLiveData()
    val title: MutableLiveData<String> = MutableLiveData()
    val subTitle: MutableLiveData<String> = MutableLiveData()

    private val progressListener = object : ProgressListener {
        override fun onProgressUpdated(p0: Long, p1: Long) {
            if (!seekbarTouched) {
                currentPosition.postValue(p0.toInt())
            }
            mediaLength.postValue(p1)
            isPlaying.postValue(cast.getState())
        }
    }

    fun registerProgressListener() {
        cast.registerCastProgressListener(
            progressListener
        )
    }

    fun unregisterProgressListener() {
        cast.unregisterCastProgressListener(progressListener)
    }

    fun touch() {
        seekbarTouched = true
    }

    fun seekTo(position: Int?) {
        cast.seekToPosition(position)
    }

    fun seekToEnd() {
        seekTo(mediaLength.value?.toInt())
    }

    fun seekTenSecondsBack() {
        val newPosition = currentPosition.value?.minus(TEN_SECONS)
        seekTo(newPosition)
    }

    fun seekTenSecondsForward() {
        val newPosition = currentPosition.value?.plus(TEN_SECONS)
        seekTo(newPosition)
    }

    fun touchEnd() {
        seekTo(currentPosition.value)
        seekbarTouched = false
    }

    fun playPauseClick() {
        if (this.isPlaying.value == true) {
            cast.pause()
        } else {
            cast.play()
        }
    }

    companion object {
        const val TEN_SECONS = 10000;
    }

}