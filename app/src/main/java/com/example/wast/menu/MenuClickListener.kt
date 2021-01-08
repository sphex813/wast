package com.example.wast.menu

import com.example.wast.models.SearchType

interface MenuClickListener {
    fun onClick(searchType: SearchType)
}