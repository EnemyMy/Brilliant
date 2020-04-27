package com.example.app_37_brilliantapp.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.widget.EditText
import android.widget.TimePicker
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.FragmentTransaction
import com.example.app_37_brilliantapp.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

enum class Time {
    DAYS,
    HOURS,
    MINUTES,
    SECONDS
}

private val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US)

fun FragmentTransaction.addSlideAnimation(): FragmentTransaction {
    this.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
    return this
}

fun String.isValidEmail() =
    this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword() = this.isNotEmpty() && this.length >= 8 && this.contains(Regex("\\d"))

fun String.isValidName() = this.isNotEmpty()


fun EditText.showDateTimePickerDialog() {
    val calendarFromEditText = text.toString().toCalendar()
    val startYear = calendarFromEditText.get(Calendar.YEAR)
    val startMonth = calendarFromEditText.get(Calendar.MONTH)
    val startDay = calendarFromEditText.get(Calendar.DAY_OF_MONTH)
    val startHour = calendarFromEditText.get(Calendar.HOUR_OF_DAY)
    val startMinute = calendarFromEditText.get(Calendar.MINUTE)

    val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, day ->
        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val resultCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
            when {
                resultCalendar.after(Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }) -> setText(resultCalendar.toFormattedString())
                resultCalendar.after(Calendar.getInstance()) -> rootView.showSnackbar(SnackbarEvent("Polish time must be at least 1 hour"))
                else -> rootView.showSnackbar(SnackbarEvent("Pls choose correct time"))
            }
        }, startHour, startMinute, DateFormat.is24HourFormat(context)).show()
    }, startYear, startMonth, startDay)
    datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
    datePickerDialog.show()
}

fun Calendar.toFormattedString(): String = df.format(time)

fun String.toCalendar(): Calendar = Calendar.getInstance().apply {
    time = toDateOrToday()
}

fun String.toDateOrToday(): Date = try {
    df.parse(this) ?: Date()
} catch (e: ParseException) {
    Date()
}

fun Long.differenceToCurrentInTime(): String {
    val currentTime = Date().time
    if (this < currentTime)
        return ""

    val remainingTime = this - currentTime
    val days = TimeUnit.MILLISECONDS.toDays(remainingTime)
    val remainingHoursInMillis = remainingTime - TimeUnit.DAYS.toMillis(days)
    val hours = TimeUnit.MILLISECONDS.toHours(remainingHoursInMillis)
    val remainingMinutesInMillis =
            remainingHoursInMillis - TimeUnit.HOURS.toMillis(hours)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMinutesInMillis)
    val remainingSecondsInMillis =
            remainingMinutesInMillis - TimeUnit.MINUTES.toMillis(minutes)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingSecondsInMillis)
    return when {
        days > 0 -> getTimeStringOrEmpty(days, Time.DAYS) + getTimeStringOrEmpty(hours, Time.HOURS)
        hours > 0 -> getTimeStringOrEmpty(hours, Time.HOURS) + getTimeStringOrEmpty(minutes, Time.MINUTES)
        minutes > 0 -> getTimeStringOrEmpty(minutes, Time.MINUTES) + getTimeStringOrEmpty(seconds, Time.SECONDS)
        else -> getTimeStringOrEmpty(seconds, Time.SECONDS)
    }
}

fun Long.differenceToGivenInTime(given: Long): String {
    if (this < given)
        return ""

    val remainingTime = this - given
    val days = TimeUnit.MILLISECONDS.toDays(remainingTime)
    val remainingHoursInMillis = remainingTime - TimeUnit.DAYS.toMillis(days)
    val hours = TimeUnit.MILLISECONDS.toHours(remainingHoursInMillis)
    val remainingMinutesInMillis =
            remainingHoursInMillis - TimeUnit.HOURS.toMillis(hours)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(remainingMinutesInMillis)
    val remainingSecondsInMillis =
            remainingMinutesInMillis - TimeUnit.MINUTES.toMillis(minutes)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(remainingSecondsInMillis)
    return when {
        days > 0 -> getTimeStringOrEmpty(days, Time.DAYS) + getTimeStringOrEmpty(hours, Time.HOURS)
        hours > 0 -> getTimeStringOrEmpty(hours, Time.HOURS) + getTimeStringOrEmpty(minutes, Time.MINUTES)
        minutes > 0 -> getTimeStringOrEmpty(minutes, Time.MINUTES) + getTimeStringOrEmpty(seconds, Time.SECONDS)
        else -> getTimeStringOrEmpty(seconds, Time.SECONDS)
    }
}

private fun getTimeStringOrEmpty(value: Long, type: Time): String {
    val str = when(type) {
        Time.DAYS -> " day"
        Time.HOURS -> " hour"
        Time.MINUTES -> " minute"
        Time.SECONDS -> " second"
    }

    return when(value) {
        0L -> ""
        1L -> "$value $str "
        else -> "$value ${str}s "
    }
}

