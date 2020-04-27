package com.example.app_37_brilliantapp.getdiamond

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.StateViewModelFactory
import com.example.app_37_brilliantapp.databinding.FragmentGetDiamondBinding
import com.example.app_37_brilliantapp.util.setupSnackbar
import javax.inject.Inject
import javax.inject.Named

class GetDiamondFragment @Inject constructor(@Named("GetDiamondViewModelFactory") private val factory: BaseViewModelFactory<out ViewModel>): Fragment() {

    private lateinit var binding: FragmentGetDiamondBinding
    private val viewModel by viewModels<GetDiamondViewModel> { StateViewModelFactory(factory, this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGetDiamondBinding.inflate(inflater).apply {
            this.fragment = this@GetDiamondFragment
            this.viewModel = this@GetDiamondFragment.viewModel
            this.lifecycleOwner = this@GetDiamondFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupSnackbar()
    }

    private fun setupNavigation() {
        viewModel.diamondSuccessfullySavedEvent.observe(this, Observer {
            onBackButtonClick()
        })
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarEvent)
    }

    fun onBackButtonClick() {
        requireActivity().supportFragmentManager.popBackStack()
    }
}