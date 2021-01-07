package com.example.websharecaster.menu

import com.example.websharecaster.models.SearchType

interface MenuClickListener {
    fun onClick(searchType: SearchType)
}