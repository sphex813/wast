package com.example.wast.splashScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.fragment.findNavController
import com.example.wast.R
import com.example.wast.databinding.SplashScreenBinding
import com.example.wast.main.MainActivityViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreen : Fragment() {
    private lateinit var launchJob: Job
    private val mainViewModel by sharedViewModel<MainActivityViewModel>()
    private val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        mainViewModel.disableMenu(true)
        val binding: SplashScreenBinding = DataBindingUtil.inflate(inflater, R.layout.splash_screen, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchJob = lifecycleScope.launch(Dispatchers.Default) {
            val isLoggedIn = viewModel.isLoggedIn()
            delay(1000)

            whenResumed {
                withContext(Dispatchers.Main) {
                    findNavController().navigate(
                        if (isLoggedIn) {
                            SplashScreenDirections.actionSplashScreenToHomeFragment()
                        } else {
                            SplashScreenDirections.actionSplashScreenToLoginFragment()

                        }
                    )
                }
            }
        }
    }
}