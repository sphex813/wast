package com.example.wast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.wast.api.models.SccData
import com.example.wast.databinding.FragmentSeriesBinding
import com.example.wast.search.MovieClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match

class SeriesFragment : Fragment(), MovieClickListener {
    private val movieAdapter = MovieAdapter(this)
    private val myViewModel: SeriesViewModel by viewModel()
    val args: SeriesFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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

        myViewModel.getSeries(args.tvShowId!!)

        myViewModel.data.observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })
    }

    override fun onItemClick(movie: SccData) {
        if (movie._source.children_count > 0) {
            findNavController().navigate(SeriesFragmentDirections.actionSeriesFragmentToEpisodesFragment(movie._id, args.showImage))
        } else {
            // TODO pusti cast
        }
    }
}