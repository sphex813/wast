package com.example.wast.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.wast.R
import com.example.wast.api.models.SccData
import com.example.wast.databinding.FragmentSearchBinding
import com.example.wast.dialog.StreamSelectDialog
import com.example.wast.listeners.MenuSearchClickListener
import com.example.wast.main.MainActivity
import com.example.wast.main.MainActivityViewModel
import com.example.wast.models.LoadingState
import com.example.wast.models.SearchType
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment(), MovieClickListener, HistoryClickListener, MenuSearchClickListener {
    val mainViewModel by sharedViewModel<MainActivityViewModel>()
    private val myViewModel: SearchViewModel by viewModel()
    private val movieAdapter = MovieAdapter(this)
    private val historyAdapter = SearchHistoryAdapter(this)
    private val args: SearchFragmentArgs by navArgs()
    val imm: InputMethodManager by lazy {
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = myViewModel
        binding.movieAdapter = movieAdapter
        binding.historyAdapter = historyAdapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setKeyboardActionListener()
        setMenuSearchButtonListener()
        setSearchFocusListener()
        setBackNavigationListener()

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

        myViewModel.getSearchHistory()
        setObservers()
    }

    private fun setObservers() {
        myViewModel.data.observe(viewLifecycleOwner, {
            movieAdapter.submitList(it)
        })

        myViewModel.history.observe(viewLifecycleOwner, {
            historyAdapter.submitList(it)
        })

        myViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it.status == LoadingState.Status.FAILED) {
                Toast.makeText(activity, it.msg!!, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setBackNavigationListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (et_search.hasFocus()) {
                et_search.clearFocus()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun setSearchFocusListener() {
        et_search.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                myViewModel.showHistory.postValue(true)
            } else {
                myViewModel.showHistory.postValue(false)
            }
        }
    }

    private fun setMenuSearchButtonListener() {
        (activity as MainActivity?)?.setSearchListener(this)
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

    private fun clearSearchEditText(clearSearchBar: Boolean = true) {
        if (clearSearchBar) {
            et_search.text.clear()
        }
        et_search.clearFocus()
        closeKeyboard()
    }

    private fun closeKeyboard() {
        imm.hideSoftInputFromWindow(et_search.windowToken, 0)
    }

    override fun onItemClick(mediaData: SccData) {
        if (mediaData._source.children_count > 0) {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToSeriesFragment(mediaData._id, mediaData))
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                val streams = myViewModel.getStreams(mediaData._id)
                withContext(Dispatchers.Main) {
                    if (streams.isEmpty()) {
                        Toast.makeText(context, "Pre túto epizódu neexistuje žiadny stream", Toast.LENGTH_SHORT).show()
                    } else {
                        StreamSelectDialog(streams, mediaData).show(parentFragmentManager, "streamSelect")
                    }
                }
            }
        }
    }

    override fun onHistoryClick(historyItem: String) {
        myViewModel.search(historyItem)
        clearSearchEditText(false)
    }

    override fun onDeleteHistoryClick(historyItem: String) {
        myViewModel.deleteFromHistory(historyItem)
    }

    override fun onMenuSearchButtonClick() {
        if (et_search.hasFocus()) {
            myViewModel.search()
            clearSearchEditText()
        } else {
            focusSearchEditText()
        }
    }


}