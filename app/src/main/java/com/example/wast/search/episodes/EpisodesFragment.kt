package com.example.wast.search.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.wast.search.MovieAdapter
import com.example.wast.R
import com.example.wast.api.models.SccData
import com.example.wast.databinding.FragmentEpisodesBinding
import com.example.wast.dialog.StreamSelectDialog
import com.example.wast.main.MainActivityViewModel
import com.example.wast.search.MovieClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EpisodesFragment : Fragment(), MovieClickListener {
    val args: EpisodesFragmentArgs by navArgs()
    val movieAdapter = MovieAdapter(this)
    private val myViewModel: EpisodesViewModel by viewModel()
    private val mainViewModel: MainActivityViewModel by sharedViewModel()

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

        mainViewModel.watchedHistory.observe(viewLifecycleOwner, {
            movieAdapter.notifyDataSetChanged()
        })

        myViewModel.data.observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })
    }

    override fun onItemClick(episodeData: SccData) {
        CoroutineScope(Dispatchers.IO).launch {
            val streams = myViewModel.getStreams(episodeData._id)
            withContext(Dispatchers.Main) {
                if (streams.isEmpty()) {
                    Toast.makeText(context, "Pre túto epizódu neexistuje žiadny stream", Toast.LENGTH_SHORT).show()
                } else {
                    StreamSelectDialog(streams, episodeData, args.parentMediaData).show(parentFragmentManager, "streamSelect")
                }
            }
        }
    }


}