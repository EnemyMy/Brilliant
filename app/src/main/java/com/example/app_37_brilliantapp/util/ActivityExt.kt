package com.example.app_37_brilliantapp.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.navigateToFragment(
        fragmentClass: Class<out Fragment>,
        container: Int,
        isAnimated: Boolean,
        backStackTag: String? = ""
) {
    supportFragmentManager.beginTransaction()
            .also {
                if (isAnimated)
                    it.addSlideAnimation()
            }
            .replace(container, fragmentClass, null)
            .also {
                if (backStackTag != "")
                    it.addToBackStack(backStackTag)
            }
            .commit()
}