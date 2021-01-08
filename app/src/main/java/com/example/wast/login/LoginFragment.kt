package com.example.wast.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.wast.R
import com.example.wast.databinding.FragmentLoginBinding
import com.example.wast.datastore.PreferenceKeys
import com.example.wast.listeners.SingleClickListener
import com.example.wast.main.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment(), SingleClickListener {
    private val myViewModel: LoginViewModel by viewModel()
    val mainViewModel by sharedViewModel<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = myViewModel
        binding.clickListener = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            myViewModel.userName.postValue(myViewModel.getFromStorage(PreferenceKeys.USER_NAME))
        }
        CoroutineScope(Dispatchers.IO).launch {
            myViewModel.password.postValue(myViewModel.getFromStorage(PreferenceKeys.PASSWORD))
        }

        myViewModel.userName.observe(viewLifecycleOwner, Observer {
            myViewModel.isLoginValid.postValue(true)
        })

        myViewModel.password.observe(viewLifecycleOwner, Observer {
            myViewModel.isLoginValid.postValue(true)
        })
    }

    override fun onClick() {
        loginWithSalt()
    }

    private fun loginWithSalt() {
        CoroutineScope(Dispatchers.IO).launch {
            val loginChanged = myViewModel.loginChanged()
            val hasToken = myViewModel.hasToken()
            if (!hasToken || loginChanged) {
                val salt = myViewModel.salt().body()?.salt ?: ""
                val token = myViewModel.login(salt)?.body()?.token
                if (token != null) {
                    myViewModel.saveLoginData(token)
                    withContext(Dispatchers.Main) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMenuFragment())
                    }
                } else {
                    myViewModel.isLoginValid.postValue(false)
                }
            } else {
                withContext(Dispatchers.Main) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMenuFragment())
                }
            }
        }
    }

}