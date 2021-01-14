package com.example.wast.dialog

import StreamSelectViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.wast.api.models.StreamInfo
import com.example.wast.databinding.StreamSelectItemBinding
import com.example.wast.viewHolders.BaseViewHolder

class StreamSelectAdapter(private val listener: StreamInfoClickListener) : ListAdapter<StreamInfo, BaseViewHolder<*>>(Companion) {
    companion object : DiffUtil.ItemCallback<StreamInfo>() {
        override fun areItemsTheSame(oldItem: StreamInfo, newItem: StreamInfo): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: StreamInfo, newItem: StreamInfo): Boolean =
            oldItem._id == newItem._id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StreamSelectViewHolder(StreamSelectItemBinding.inflate(layoutInflater, parent, false), listener, parent.context)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        (holder as StreamSelectViewHolder).bind(getItem(position))
    }
}