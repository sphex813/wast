package com.example.wast.main

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.mediarouter.app.MediaRouteButton
import androidx.navigation.findNavController
import com.example.wast.NavGraphDirections
import com.example.wast.R
import com.example.wast.databinding.ActivityMainBinding
import com.example.wast.listeners.SingleClickListener
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), SingleClickListener {
    private val viewModel by viewModel<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val navController
        get() = findNavController(R.id.fragment_container)

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
        if (navController.currentDestination?.id != R.id.searchFragment) {
            navController.navigate(NavGraphDirections.actionGlobalSearchFragment())
        } else {
            //TODO refaktor https://stackoverflow.com/questions/14247954/communicating-between-a-fragment-and-an-activity-best-practices
            et_search.requestFocus()
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }


    }
}