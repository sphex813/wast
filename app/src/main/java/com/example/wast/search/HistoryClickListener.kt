package com.example.wast.search

interface HistoryClickListener {
    fun onHistoryClick(historyItem: String)
    fun onDeleteHistoryClick(position: Int)
}