package com.example.app_37_brilliantapp.custom

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class FontSpan(private val font: Typeface?) : MetricAffectingSpan() {
    override fun updateMeasureState(textPaint: TextPaint) = update(textPaint)
    override fun updateDrawState(textPaint: TextPaint?) = update(textPaint)

    private fun update(tp: TextPaint?) {
        tp.apply {
            val old = this!!.typeface
            val oldStyle = old?.style ?: 0
            val font = Typeface.create(font, oldStyle)
            typeface = font
        }
    }
}