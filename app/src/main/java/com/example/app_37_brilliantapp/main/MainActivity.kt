package com.example.app_37_brilliantapp.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import com.example.app_37_brilliantapp.application.BrilliantApplication
import com.example.app_37_brilliantapp.R
import com.example.app_37_brilliantapp.signup.SignupFragment
import com.example.app_37_brilliantapp.util.navigateToFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        (application as BrilliantApplication).appComponent.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.navigateToFragment(SignupFragment::class.java, R.id.main_activity_fragment_container, false)
        }
    }
}