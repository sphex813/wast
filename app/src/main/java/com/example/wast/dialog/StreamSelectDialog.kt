package com.example.wast.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.wast.api.models.SccData
import com.example.wast.api.models.StreamInfo
import com.example.wast.databinding.DialogStreamSelectBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class StreamSelectDialog(private val streams: List<StreamInfo>, private val media: SccData) : DialogFragment(), StreamInfoClickListener {
    private val viewModel: StreamSelectViewModel by viewModel()
    private val streamSelectAdapter = StreamSelectAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding: DialogStreamSelectBinding = DialogStreamSelectBinding.inflate(LayoutInflater.from(context))
        binding.adapter = streamSelectAdapter
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        streamSelectAdapter.submitList(streams)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onItemClick(info: StreamInfo) {
        viewModel.playMedia(media, info.ident)
        dialog?.dismiss()
    }
}