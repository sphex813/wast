package com.example.wast.search

import com.example.wast.api.models.SccData

interface MovieClickListener {
    fun onItemClick(movie: SccData)
}