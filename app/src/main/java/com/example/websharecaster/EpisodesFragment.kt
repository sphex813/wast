package com.example.websharecaster

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.websharecaster.api.models.SccData
import com.example.websharecaster.databinding.FragmentEpisodesBinding
import com.example.websharecaster.dialog.StreamSelectDialog
import com.example.websharecaster.search.MovieAdapter
import com.example.websharecaster.search.MovieClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodesFragment : Fragment(), MovieClickListener {
    val args: EpisodesFragmentArgs by navArgs()
    val movieAdapter = MovieAdapter(this)
    private val myViewModel: EpisodesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentEpisodesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_episodes, container, false)
        binding.lifecycleOwner = this
        binding.clickListener = this
        binding.viewModel = myViewModel
        binding.movieAdapter = movieAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myViewModel.getEpisodes(args.seriesId!!)

        myViewModel.data.observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })
    }

    override fun onItemClick(media: SccData) {
        CoroutineScope(Dispatchers.IO).launch {
            val streams = myViewModel.getStreams(media._id)
            withContext(Dispatchers.Main) {
                StreamSelectDialog(streams, media).show(parentFragmentManager, "streamSelect")
            }
        }
    }
}