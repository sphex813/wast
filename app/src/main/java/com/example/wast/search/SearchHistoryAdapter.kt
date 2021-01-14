package com.example.wast.search

import SearchHistoryViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.wast.databinding.SearchHistoryItemBinding
import com.example.wast.view_holders.BaseViewHolder

class SearchHistoryAdapter(private val listener: HistoryClickListener) : ListAdapter<String, BaseViewHolder<*>>(Companion) {
    companion object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem.equals(newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SearchHistoryViewHolder(SearchHistoryItemBinding.inflate(layoutInflater, parent, false), listener)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        (holder as SearchHistoryViewHolder).bind(getItem(position), position)
    }
}