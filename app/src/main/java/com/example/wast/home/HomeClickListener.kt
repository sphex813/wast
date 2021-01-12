package com.example.wast.home

import com.example.wast.models.SearchType

interface HomeClickListener {
    fun onClick(searchType: SearchType)
}