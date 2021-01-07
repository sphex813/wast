package com.example.websharecaster.search

import com.example.websharecaster.api.models.SccData

interface MovieClickListener {
    fun onItemClick(movie: SccData)
}