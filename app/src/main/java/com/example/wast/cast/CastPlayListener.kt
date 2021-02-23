package com.example.wast.cast

import com.example.wast.api.models.SccData

interface CastPlayListener {
    fun addToWatched()
    fun setPlayiedMedia(media: SccData)
}