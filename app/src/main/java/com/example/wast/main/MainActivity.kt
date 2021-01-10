package com.example.wast.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.mediarouter.app.MediaRouteButton
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.wast.R
import com.example.wast.databinding.ActivityMainBinding
import com.example.wast.listeners.SingleClickListener
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), SingleClickListener {
    private val viewModel by viewModel<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setCastContext(CastContext.getSharedInstance(this))

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.clickListener = this
        binding.lifecycleOwner = this

        val mMediaRouteButton = findViewById<MediaRouteButton>(R.id.media_route_button);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mMediaRouteButton);
    }

    override fun onClick() {
        supportFragmentManager.findFragmentById(R.id.fragment_container)?.findNavController()

    }
}