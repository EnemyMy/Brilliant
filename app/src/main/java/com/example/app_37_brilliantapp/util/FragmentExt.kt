package com.example.app_37_brilliantapp.util

import androidx.fragment.app.Fragment

fun Fragment.navigateToFragment(
        fragmentClass: Class<out Fragment>,
        container: Int,
        isAnimated: Boolean,
        backStackTag: String? = ""
) {
    childFragmentManager.beginTransaction()
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