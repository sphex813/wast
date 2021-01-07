package com.example.websharecaster.dialog

import com.example.websharecaster.api.models.StreamInfo

interface StreamInfoClickListener {
    fun onItemClick(info: StreamInfo)
}