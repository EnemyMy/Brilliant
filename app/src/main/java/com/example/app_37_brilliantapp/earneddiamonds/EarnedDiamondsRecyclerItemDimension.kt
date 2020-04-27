package com.example.app_37_brilliantapp.earneddiamonds

import android.content.Context
import android.content.res.Configuration

class EarnedDiamondsRecyclerItemDimension(context: Context) {
    private val screenWidth = context.resources.displayMetrics.widthPixels
    private val currentOrientation = context.resources.configuration.orientation
    var totalItemsOnPortraitOrientation = 2
    var totalItemsOnLandscapeOrientation = totalItemsOnPortraitOrientation + 3
    val itemSpacing = when (currentOrientation) {
        Configuration.ORIENTATION_PORTRAIT -> (screenWidth / 8F) / (totalItemsOnPortraitOrientation + 1)
        else -> (screenWidth / 8F) / (totalItemsOnLandscapeOrientation + 1)
    }
    val itemWidth = when (currentOrientation) {
        Configuration.ORIENTATION_PORTRAIT -> (screenWidth - (screenWidth / 8F)) / totalItemsOnPortraitOrientation
        else -> (screenWidth - (screenWidth / 8F)) / totalItemsOnLandscapeOrientation
    }
}