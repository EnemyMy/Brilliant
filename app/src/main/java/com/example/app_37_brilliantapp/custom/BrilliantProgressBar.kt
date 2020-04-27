package com.example.app_37_brilliantapp.custom

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.app_37_brilliantapp.R
import com.example.app_37_brilliantapp.data.CurrentDiamond
import com.example.app_37_brilliantapp.util.differenceToCurrentInTime
import kotlin.math.roundToInt

class BrilliantProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    lateinit var updater: BrilliantProgressBarUpdater
    private val paint = Paint()
    private val textPaint = TextPaint()
    private var oval = RectF()
    private var firstDigitAnimatedDrawable = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_pathmorph_digits_1)
    private var secondDigitAnimatedDrawable = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_pathmorph_digits_1)
    private var thirdDigitAnimatedDrawable = AnimatedVectorDrawableCompat.create(context, R.drawable.avd_pathmorph_digits_0_to_1)
    private var progress = 0
    private var timeLeftText = ""
    private val percentsTextSize by lazy { measuredWidth / 6F }
    private val percentsTextBounds by lazy { Rect(
            (measuredWidth / 3.4).roundToInt(),
            (measuredHeight / 2.2).roundToInt() - (percentsTextSize / 2).roundToInt(),
            (measuredWidth / 3.4).roundToInt() + percentsTextSize.roundToInt() - (percentsTextSize * 0.1).roundToInt(),
            (measuredHeight / 2.2).roundToInt() + (percentsTextSize / 2).roundToInt()) }
    private val percentsDigitSpacing by lazy { measuredWidth / 8 }
    private val percentSignTextSize by lazy { percentsTextSize / 2.5F }
    private val timeLeftTextSize by lazy { percentsTextSize / 3.5F }

    override fun onDraw(canvas: Canvas?) {

        //draw progress line
        paint.color = resources.getColor(R.color.progressBarColor)
        paint.isAntiAlias = true
        paint.strokeWidth = measuredWidth / 12.769F
        paint.style = Paint.Style.FILL
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        oval.set(0F,0F, measuredWidth.toFloat(), measuredHeight.toFloat())
        oval.inset(paint.strokeWidth / 2, paint.strokeWidth / 2)
        canvas!!.drawArc(oval, 135F, progress * ((360 * 0.75F) / 100), false, paint)

        //draw first percent digit
        if (progress == 100) {
            firstDigitAnimatedDrawable!!.bounds = percentsTextBounds
            firstDigitAnimatedDrawable!!.draw(canvas)
            percentsTextBounds.offset(percentsDigitSpacing - percentsDigitSpacing / 4, 0)
        }
        else
            percentsTextBounds.offset(percentsDigitSpacing / 2, 0)

        //draw second percent digit
        if (progress >= 10) {
            secondDigitAnimatedDrawable!!.bounds = percentsTextBounds
            secondDigitAnimatedDrawable!!.draw(canvas)
            percentsTextBounds.offset(percentsDigitSpacing, 0)
        }
        else
            percentsTextBounds.offset(percentsDigitSpacing / 2, 0)

        //draw third percent digit
        thirdDigitAnimatedDrawable!!.bounds = percentsTextBounds
        thirdDigitAnimatedDrawable!!.draw(canvas)
        if (firstDigitAnimatedDrawable!!.isRunning || secondDigitAnimatedDrawable!!.isRunning || thirdDigitAnimatedDrawable!!.isRunning) invalidate()

        //draw percent sign
        textPaint.color = Color.WHITE
        textPaint.typeface = ResourcesCompat.getFont(context, R.font.montserrat_light)
        textPaint.textSize = percentSignTextSize
        canvas.drawText("%", percentsTextBounds.right.toFloat(), percentsTextBounds.top.toFloat() + percentSignTextSize, textPaint)

        //draw "time left" text
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = timeLeftTextSize
        canvas.drawText(timeLeftText, measuredWidth.toFloat() / 2, measuredHeight.toFloat() / 2 + textPaint.textSize + (percentsTextSize / 2), textPaint)

        //draw "time remaining" text
        canvas.drawText("Time remaining", measuredWidth.toFloat() / 2, measuredHeight * 0.85F, textPaint)

        percentsTextBounds.set((measuredWidth / 3.4).roundToInt(),
                (measuredHeight / 2.2).roundToInt() - (percentsTextSize / 2).roundToInt(),
                (measuredWidth / 3.4).roundToInt() + percentsTextSize.roundToInt() - (percentsTextSize * 0.1).roundToInt(),
                (measuredHeight / 2.2).roundToInt() + (percentsTextSize / 2).roundToInt())
    }

    fun setProgress(progress: Int) {
        if (progress < 0 || progress > 100)
            return
        val secondDigit = (progress / 10) % 10
        val thirdDigit = progress % 10
        val showSecondDigit = ((thirdDigit == 0 && progress > 0) || (this.progress == 0 && progress >= 10))
        this.progress = progress

        if (showSecondDigit) {
            secondDigitAnimatedDrawable = getAnimatedDrawableFromDigit(secondDigit)
        }
        thirdDigitAnimatedDrawable = getAnimatedDrawableFromDigit(thirdDigit)
        postInvalidateOnAnimation()
        firstDigitAnimatedDrawable!!.start()
        if (showSecondDigit)
            secondDigitAnimatedDrawable!!.start()
        thirdDigitAnimatedDrawable!!.start()
    }

    private fun getAnimatedDrawableFromDigit(digit: Int): AnimatedVectorDrawableCompat {
        val drawableResource = when(digit) {
            1 -> R.drawable.avd_pathmorph_digits_0_to_1
            2 -> R.drawable.avd_pathmorph_digits_1_to_2
            3 -> R.drawable.avd_pathmorph_digits_2_to_3
            4 -> R.drawable.avd_pathmorph_digits_3_to_4
            5 -> R.drawable.avd_pathmorph_digits_4_to_5
            6 -> R.drawable.avd_pathmorph_digits_5_to_6
            7 -> R.drawable.avd_pathmorph_digits_6_to_7
            8 -> R.drawable.avd_pathmorph_digits_7_to_8
            9 -> R.drawable.avd_pathmorph_digits_8_to_9
            else -> R.drawable.avd_pathmorph_digits_9_to_0
        }
        return AnimatedVectorDrawableCompat.create(context, drawableResource)!!
    }

    fun setTimeLeft(deadline: Long) {
        timeLeftText = deadline.differenceToCurrentInTime()
        invalidate()
    }

    fun startUpdate(diamond: CurrentDiamond) {
        updater.startUpdate(this, diamond)
    }

    fun stopUpdate() {
        updater.stopUpdate()
    }

}