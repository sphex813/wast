package com.example.wast.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wast.R
import com.example.wast.models.SearchType
import com.example.wast.databinding.FragmentMenuBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenuFragment : Fragment(), MenuClickListener {
    private val myViewModel: MenuViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentMenuBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_menu,
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
        findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToSearchFragment())

    }
}