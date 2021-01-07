package com.example.websharecaster.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.websharecaster.R
import com.example.websharecaster.api.models.SccData
import com.example.websharecaster.api.models.StreamInfo
import com.example.websharecaster.databinding.FragmentSearchBinding
import com.example.websharecaster.dialog.StreamSelectDialog
import com.example.websharecaster.main.MainActivityViewModel
import com.example.websharecaster.models.LoadingState
import com.example.websharecaster.models.SearchType
import com.example.websharecaster.utils.HelpUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment() : Fragment(), MovieClickListener {
    val mainViewModel by sharedViewModel<MainActivityViewModel>()
    private val myViewModel: SearchViewModel by viewModel()
    private val movieAdapter = MovieAdapter(this)
    private var movie: SccData? = null
    private val args: SearchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = myViewModel
        binding.movieAdapter = movieAdapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (args.searchType) {
            SearchType.MOVIES -> myViewModel.getMovies()
            SearchType.MOVIES_DUBBED -> myViewModel.getMoviesCzsk()
            SearchType.SHOWS -> myViewModel.getTvshows()
            SearchType.SHOWS_DUBBED -> myViewModel.getTvshowsCzsk()
            SearchType.DEFAULT -> {
            }
        }

        myViewModel.searchedFileName.observe(viewLifecycleOwner, {
            // TODO na enter klavesnice
            myViewModel.searchWithDebounce()
        })

        myViewModel.data.observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })

        myViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it.status == LoadingState.Status.FAILED) {
                Toast.makeText(activity, it.msg!!, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(media: SccData) {
        if (media._source.children_count > 0) {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSeriesFragment(media._id, HelpUtils.getMovieLink(media._source.i18n_info_labels)))
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val streams: List<StreamInfo> = myViewModel.getStreams(media._id)

                val languageHashMap: HashMap<String, Int> = hashMapOf(
                    "sk" to 0,
                    "cz" to 1,
                    "en" to 2,
                )

                val sortedList = streams.sortedWith(compareBy({ languageHashMap[it.audio?.get(0)?.language] }, { it.size!!.toBigInteger() }))
                withContext(Dispatchers.Main) {
                    StreamSelectDialog(sortedList, media).show(parentFragmentManager, "streamSelect")
                }
            }
        }
    }
}