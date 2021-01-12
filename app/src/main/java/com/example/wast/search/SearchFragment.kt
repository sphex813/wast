package com.example.wast.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.wast.R
import com.example.wast.api.models.SccData
import com.example.wast.api.models.StreamInfo
import com.example.wast.databinding.FragmentSearchBinding
import com.example.wast.dialog.StreamSelectDialog
import com.example.wast.listeners.OnSearchListener
import com.example.wast.main.MainActivity
import com.example.wast.main.MainActivityViewModel
import com.example.wast.models.LoadingState
import com.example.wast.models.SearchType
import com.example.wast.utils.HelpUtils
import kotlinx.android.synthetic.main.fragment_search.*
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
    private val args: SearchFragmentArgs by navArgs()
    private var movie: SccData? = null
    val imm: InputMethodManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

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
        if (myViewModel.data.value == null) {
            when (args.searchType) {
                SearchType.MOVIES -> myViewModel.getMovies()
                SearchType.MOVIES_DUBBED -> myViewModel.getMoviesCzsk()
                SearchType.SHOWS -> myViewModel.getTvshows()
                SearchType.SHOWS_DUBBED -> myViewModel.getTvshowsCzsk()
                SearchType.DEFAULT -> {
                    focusSearchEditText()
                }
            }
        }
        myViewModel.data.observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })

        myViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it.status == LoadingState.Status.FAILED) {
                Toast.makeText(activity, it.msg!!, Toast.LENGTH_SHORT).show()
            }
        })

        setKeyboardActionListener()
        setMainActivityListener()
    }

    private fun setMainActivityListener() {
        (activity as MainActivity?)?.setSearchListener(object : OnSearchListener {
            override fun search() {
                myViewModel.search()
                clearSearchEditText()
            }

            override fun searchFocus() {
                focusSearchEditText()
            }
        })
    }

    private fun focusSearchEditText() {
        et_search.requestFocus()
        appbar.setExpanded(true, true)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun setKeyboardActionListener() {
        et_search.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    myViewModel.search()
                    clearSearchEditText()
                    true
                }
                else -> false
            }
        }
    }

    private fun clearSearchEditText() {
        et_search.text.clear()
        et_search.clearFocus()
        closeKeyboard()
    }

    private fun closeKeyboard() {
        imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0)
    }

    override fun onItemClick(media: SccData) {
        if (media._source.children_count > 0) {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSeriesFragment(media._id,
                HelpUtils.getMovieLink(media._source.i18n_info_labels)))
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