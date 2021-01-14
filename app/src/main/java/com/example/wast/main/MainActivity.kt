package com.example.wast.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.mediarouter.app.MediaRouteButton
import androidx.navigation.findNavController
import com.example.wast.NavGraphDirections
import com.example.wast.R
import com.example.wast.databinding.ActivityMainBinding
import com.example.wast.listeners.MenuClickListener
import com.example.wast.listeners.OnSearchListener
import com.example.wast.models.SearchType
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), MenuClickListener {
    var onSearchListener: OnSearchListener? = null
    private val viewModel by viewModel<MainActivityViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val navController
        get() = findNavController(R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setCastContext(CastContext.getSharedInstance(this))
        viewModel.registerOnCustSuccesfullListener()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.clickListener = this
        binding.lifecycleOwner = this

        val mMediaRouteButton = findViewById<MediaRouteButton>(R.id.media_route_button);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), mMediaRouteButton);
    }

    override fun firstMenuButtonClick() {
        viewModel.logout()
        navController.navigate(NavGraphDirections.actionGlobalLoginFragment())

    }

    override fun homeMenuButtonClick() {
        navController.navigate(NavGraphDirections.actionGlobalHomeFragment())
    }

    override fun searchMenuButtonClick() {
        if (navController.currentDestination?.id != R.id.searchFragment) {
            navController.navigate(NavGraphDirections.actionGlobalSearchFragment(SearchType.DEFAULT))
        } else {
            if (et_search.hasFocus()) {
                onSearchListener?.search()
            } else {
                onSearchListener?.searchFocus()
            }
        }
    }

    override fun lastMenuButtonClick() {

    }

    fun setSearchListener(listener: OnSearchListener) {
        this.onSearchListener = listener
    }
}