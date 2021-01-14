package com.example.wast

import EpisodeViewHolder
import PosterViewHolder
import SeriesViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.wast.api.models.SccData
import com.example.wast.databinding.EpisodeBinding
import com.example.wast.databinding.MovieDetailBinding
import com.example.wast.databinding.SeriesBinding
import com.example.wast.datastore.LocalStorage
import com.example.wast.datastore.PreferenceKeys
import com.example.wast.search.MovieClickListener
import com.example.wast.viewHolders.BaseViewHolder
import okhttp3.internal.wait
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MovieAdapter(private val listener: MovieClickListener) :
    ListAdapter<SccData, BaseViewHolder<*>>(Companion) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)._source.info_labels.mediatype) {
            "season" -> 1
            "episode" -> 2
            else -> 0
        }
    }

    companion object : DiffUtil.ItemCallback<SccData>() {
        override fun areItemsTheSame(oldItem: SccData, newItem: SccData): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: SccData, newItem: SccData): Boolean =
            oldItem._id == newItem._id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            0 -> PosterViewHolder(MovieDetailBinding.inflate(layoutInflater, parent, false), listener)
            1 -> SeriesViewHolder(SeriesBinding.inflate(layoutInflater, parent, false), listener)
            2 -> EpisodeViewHolder(EpisodeBinding.inflate(layoutInflater, parent, false), listener)
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val movie = getItem(position)
        when (holder) {
            is PosterViewHolder -> holder.bind(movie as SccData)
            is SeriesViewHolder -> holder.bind(movie as SccData)
            is EpisodeViewHolder -> holder.bind(movie as SccData, position)
        }
    }

}