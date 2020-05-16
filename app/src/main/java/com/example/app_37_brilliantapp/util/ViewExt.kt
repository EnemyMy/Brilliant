package com.example.app_37_brilliantapp.util

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.app_37_brilliantapp.Event
import com.example.app_37_brilliantapp.EventObserver
import com.google.android.material.snackbar.Snackbar

fun View.setupSnackbar(owner: LifecycleOwner, event: LiveData<Event<SnackbarEvent>>) {
    event.observe(owner, EventObserver {
        showSnackbar(it)
    })
}

fun View.showSnackbar(event: SnackbarEvent) {
    Snackbar.make(this, event.text, event.length).also {
        if (event.actionText != null && event.action != null) {
            it.setAction(event.actionText, event.action)
        }
    }.show()
}

