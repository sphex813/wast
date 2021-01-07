package com.example.websharecaster.utils

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.websharecaster.models.LoadingState
import com.squareup.picasso.Picasso


object BindingAdapters {
    @JvmStatic
    @BindingAdapter("setImageUrl")
    fun ImageView.setImageUrl(url: String?) {
        if (!url.isNullOrBlank()) {
            Picasso.get()
                .load(url)
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("setAdapter")
    fun RecyclerView.bindRecycleViewAdapter(adapter: RecyclerView.Adapter<*>) {
        this.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("setupVisibility")
    fun ProgressBar.progressVisibility(loadingState: LoadingState?) {
        loadingState?.let {
            isVisible = when (it.status) {
                LoadingState.Status.RUNNING -> true
                LoadingState.Status.SUCCESS -> false
                LoadingState.Status.FAILED -> false
            }
        }
    }
}