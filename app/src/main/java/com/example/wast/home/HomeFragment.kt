package com.example.wast.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wast.R
import com.example.wast.databinding.FragmentHomeBinding
import com.example.wast.main.MainActivityViewModel
import com.example.wast.models.SearchType
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), HomeClickListener {
    val mainViewModel by sharedViewModel<MainActivityViewModel>()
    private val myViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mainViewModel.disableMenu(false)

        // Inflate the layout for this fragment
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = myViewModel
        binding.clickListener = this
        return binding.root
    }

    override fun onClick(searchType: SearchType) {
        navigate(searchType)
    }

    fun navigate(searchType: SearchType) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment(searchType))

    }
}