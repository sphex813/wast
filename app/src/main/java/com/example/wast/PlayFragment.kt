package com.example.wast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.wast.databinding.FragmentPlayBinding
import com.example.wast.main.MainActivityViewModel
import com.example.wast.utils.HelpUtils
import kotlinx.android.synthetic.main.fragment_play.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayFragment : Fragment() {
    private val myViewModel: PlayViewModel by viewModel()
    val mainViewModel by sharedViewModel<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentPlayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_play, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = myViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seekBar1.setOnSeekBarChangeListener(null);
        myViewModel.registerProgressListener();

        mainViewModel.currentlyPlaying.observe(viewLifecycleOwner, Observer {
            myViewModel.background.postValue(HelpUtils.getMovieImageLink(it._source.i18n_info_labels))
            myViewModel.title.postValue(HelpUtils.getTitle(it._source.info_labels.originaltitle ,it._source.i18n_info_labels))
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        myViewModel.unregisterProgressListener();
    }

}