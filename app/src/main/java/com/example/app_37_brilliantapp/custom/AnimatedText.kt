package com.example.app_37_brilliantapp.custom

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd

enum class StrikeType {
    NONE,
    ANIMATING,
    FULL
}

class AnimatedText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    private var strikeType = StrikeType.NONE
    var targetLength = 0f
    private val strikePaint = Paint()
    private val totalLength by lazy { width.toFloat() }
    private val startY by lazy { height.toFloat() / 2 }

    init {
        strikePaint.color = Color.BLACK
        strikePaint.isAntiAlias = true
        strikePaint.style = Paint.Style.FILL_AND_STROKE
        strikePaint.strokeWidth = 5F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (strikeType == StrikeType.ANIMATING) {
            canvas.drawLine(0F, startY, targetLength, startY, strikePaint)
        }
        if (strikeType == StrikeType.FULL) {
            canvas.drawLine(0F, startY, totalLength, startY, strikePaint)
        }
    }

    fun setTextStrikeThrough() {
        strikeType = StrikeType.FULL
        invalidate()
    }

    fun setTextCommon() {
        strikeType = StrikeType.NONE
        invalidate()
    }

    fun startStrikeThroughAnimationForward() {
        strikeType = StrikeType.ANIMATING
        val objectAnimator = ObjectAnimator.ofFloat(this, "targetLength", 0F, totalLength)
        objectAnimator.interpolator = AccelerateInterpolator()
        objectAnimator.doOnEnd { strikeType = StrikeType.FULL }
        objectAnimator.duration = 300
        objectAnimator.addUpdateListener { invalidate() }
        objectAnimator.start()
        postInvalidate()
    }

    fun startStrikeThroughAnimationBackward() {
        strikeType = StrikeType.ANIMATING
        val objectAnimator = ObjectAnimator.ofFloat(this, "targetLength", totalLength, 0F)
        objectAnimator.interpolator = AccelerateInterpolator()
        objectAnimator.doOnEnd { strikeType = StrikeType.NONE }
        objectAnimator.duration = 300
        objectAnimator.addUpdateListener { invalidate() }
        objectAnimator.start()
        postInvalidate()
    }

}