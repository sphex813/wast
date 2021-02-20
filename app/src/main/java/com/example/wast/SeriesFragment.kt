package com.example.wast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.wast.api.models.SccData
import com.example.wast.databinding.FragmentSeriesBinding
import com.example.wast.dialog.StreamSelectDialog
import com.example.wast.search.MovieClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeriesFragment : Fragment(), MovieClickListener {
    private val movieAdapter = MovieAdapter(this)
    private val myViewModel: SeriesViewModel by viewModel()
    val args: SeriesFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentSeriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_series, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = myViewModel
        binding.clickListener = this
        binding.movieAdapter = movieAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (myViewModel.data.value.isNullOrEmpty()) {
            myViewModel.getSeries(args.tvShowId!!)
        }

        myViewModel.data.observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })
    }

    override fun onItemClick(seriesData: SccData) {
        if (seriesData._source.children_count > 0) {
            findNavController().navigate(SeriesFragmentDirections.actionSeriesFragmentToEpisodesFragment(seriesData._id, args.parentMediaData))
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val streams = myViewModel.getStreams(seriesData._id)
                withContext(Dispatchers.Main) {
                    if (streams.isEmpty()) {
                        Toast.makeText(context, "Pre túto epizódu neexistuje žiadny stream", Toast.LENGTH_SHORT).show()
                    } else {
                        StreamSelectDialog(streams, seriesData, args.parentMediaData).show(parentFragmentManager, "streamSelect")
                    }
                }
            }
        }
    }
}