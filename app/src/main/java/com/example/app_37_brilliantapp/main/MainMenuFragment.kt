package com.example.app_37_brilliantapp.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentFactory
import com.example.app_37_brilliantapp.R
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.currentdiamond.CurrentDiamondFragment
import com.example.app_37_brilliantapp.databinding.FragmentMainMenuBinding
import com.example.app_37_brilliantapp.earneddiamonds.EarnedDiamondsFragment
import com.example.app_37_brilliantapp.ideas.IdeasFragment
import com.example.app_37_brilliantapp.util.navigateToFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainMenuFragment @Inject constructor(): Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: FragmentMainMenuBinding
    @Inject
    lateinit var fragmentFactory: FragmentFactory
    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.bottomNavigation.selectedItemId != R.id.currentDiamondFragment)
                binding.bottomNavigation.selectedItemId =
                    R.id.currentDiamondFragment
            else {
                if (activity!!.supportFragmentManager.backStackEntryCount > 0)
                    activity!!.supportFragmentManager.popBackStack()
                else
                    activity!!.finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as BrilliantApplication).appComponent.inject(this)
        childFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backCallback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToFragment(CurrentDiamondFragment::class.java, R.id.main_menu_fragment_container, false)
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == binding.bottomNavigation.selectedItemId)
            return false
        var selectedFragment: Class<out Fragment> = CurrentDiamondFragment::class.java
        when(item.itemId) {
            R.id.ideasFragment -> selectedFragment = IdeasFragment::class.java
            R.id.earnedDiamondsFragment -> selectedFragment = EarnedDiamondsFragment::class.java
        }
        navigateToFragment(selectedFragment, R.id.main_menu_fragment_container, true)
        return true
    }
}