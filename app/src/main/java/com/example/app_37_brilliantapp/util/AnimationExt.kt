package com.example.app_37_brilliantapp.util

import android.animation.TimeInterpolator
import android.view.View

fun View.makeAnimationScale(duration: Long, scaleX: Double, scaleY: Double, interpolator: TimeInterpolator, action: Runnable? = null) {
    animate()
        .setDuration(duration)
        .scaleX(scaleX.toFloat())
        .scaleY(scaleY.toFloat())
        .setInterpolator(interpolator)
        .withEndAction(action)
        .start()
}

fun View.makeAnimationScaleBy(duration: Long, scaleXBy: Double, scaleYBy: Double, interpolator: TimeInterpolator, action: Runnable? = null) {
    animate()
        .setDuration(duration)
        .scaleXBy(scaleXBy.toFloat())
        .scaleYBy(scaleYBy.toFloat())
        .setInterpolator(interpolator)
        .withEndAction(action)
        .start()
}