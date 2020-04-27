package com.example.app_37_brilliantapp.currentdiamond

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.app_37_brilliantapp.BaseViewModelFactory
import com.example.app_37_brilliantapp.custom.BrilliantProgressBarUpdater
import com.example.app_37_brilliantapp.R
import com.example.app_37_brilliantapp.StateViewModelFactory
import com.example.app_37_brilliantapp.databinding.FragmentCurrentDiamondBinding
import com.example.app_37_brilliantapp.finddiamond.FindTheDiamondFragment
import com.example.app_37_brilliantapp.getdiamond.GetDiamondFragment
import com.example.app_37_brilliantapp.main.MainActivity
import com.example.app_37_brilliantapp.signup.SignupFragment
import com.example.app_37_brilliantapp.util.navigateToFragment
import com.example.app_37_brilliantapp.util.setupSnackbar
import javax.inject.Inject
import javax.inject.Named

class CurrentDiamondFragment @Inject constructor(@Named("CurrentDiamondViewModelFactory") private val factory: BaseViewModelFactory<out ViewModel>): Fragment() {

    private lateinit var binding: FragmentCurrentDiamondBinding
    private val viewModel by viewModels<CurrentDiamondViewModel> { StateViewModelFactory(factory, this) }
    private val parentActivity by lazy { activity as MainActivity }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCurrentDiamondBinding.inflate(inflater).apply {
            this.fragment = this@CurrentDiamondFragment
            this.viewModel = this@CurrentDiamondFragment.viewModel
            this.lifecycleOwner = this@CurrentDiamondFragment.viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupSnackbar()
        setupProgressBar()
    }

    private fun setupProgressBar() {
        binding.brilliantProgressBar.updater = BrilliantProgressBarUpdater(this)
        viewModel.currentDiamond.observe(this, Observer {diamond ->
            if (diamond != null)
                binding.brilliantProgressBar.startUpdate(diamond)
        })
        viewModel.cancelDiamondEvent.observe(this, Observer {
            binding.brilliantProgressBar.stopUpdate()
        })
    }

    private fun setupNavigation() {
        viewModel.logoutEvent.observe(this, Observer {
            //Clear backstack first
            if (parentActivity.supportFragmentManager.backStackEntryCount > 0) {
                val entry: FragmentManager.BackStackEntry = parentActivity.supportFragmentManager.getBackStackEntryAt(0)
                parentActivity.supportFragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            parentActivity.navigateToFragment(SignupFragment::class.java, R.id.main_activity_fragment_container, true)
        })
        viewModel.findTheDiamondEvent.observe(this, Observer {
            parentActivity.navigateToFragment(FindTheDiamondFragment::class.java, R.id.main_activity_fragment_container, true, null)
        })
        viewModel.getDiamondEvent.observe(this, Observer {
            parentActivity.navigateToFragment(GetDiamondFragment::class.java, R.id.main_activity_fragment_container, true, null)
        })
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackBarEvent)
    }
}