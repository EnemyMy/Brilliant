package com.example.app_37_brilliantapp.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarEvent(val text: String, val length: Int = Snackbar.LENGTH_SHORT, val actionText: String? = null, val action: ((View) -> Unit)? = null) {
}