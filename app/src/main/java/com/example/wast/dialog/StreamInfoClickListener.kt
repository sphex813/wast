package com.example.wast.dialog

import com.example.wast.api.models.StreamInfo

interface StreamInfoClickListener {
    fun onItemClick(info: StreamInfo)
}