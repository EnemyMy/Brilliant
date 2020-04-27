package com.example.app_37_brilliantapp.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.main.MainActivity
import com.example.app_37_brilliantapp.main.MainMenuFragment
import com.example.app_37_brilliantapp.R
import com.example.app_37_brilliantapp.StateViewModelFactory
import com.example.app_37_brilliantapp.databinding.FragmentLoginBinding
import com.example.app_37_brilliantapp.util.*
import javax.inject.Inject
import javax.inject.Named

class LoginFragment @Inject constructor(@Named("LoginViewModelFactory") private val factory: BaseViewModelFactory<out ViewModel>): Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val parentActivity by lazy { activity as MainActivity }
    private val viewModel by viewModels<LoginViewModel> { StateViewModelFactory(factory, this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater).apply {
            this.fragment = this@LoginFragment
            this.viewModel = this@LoginFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupSnackBar()
    }

    private fun setupSnackBar() {
        view?.setupSnackbar(this, viewModel.snackBarEvent)
    }

    private fun setupNavigation() {
        viewModel.mainMenuEvent.observe(this, Observer {
            parentActivity.navigateToFragment(MainMenuFragment::class.java, R.id.main_activity_fragment_container, true)
        })
    }
}