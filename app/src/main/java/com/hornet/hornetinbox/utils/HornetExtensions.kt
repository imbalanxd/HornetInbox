package com.hornet.hornetinbox.utils

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.hornet.hornetinbox.R

/**
 * Extension function for Long type.
 * @return Relative time format
 */
fun Long.epochToTimeAgo(context: Context): String {
    val seconds = (System.currentTimeMillis() - this * 1000) / 1000f

    return when (true) {
        seconds < 60 -> context.resources.getQuantityString(
            R.plurals.seconds_ago,
            seconds.toInt(),
            seconds.toInt()
        )
        seconds < 3600 -> {
            val minutes = seconds / 60f
            context.resources.getQuantityString(
                R.plurals.minutes_ago,
                minutes.toInt(),
                minutes.toInt()
            )
        }
        seconds < 86400 -> {
            val hours = seconds / 3600f
            context.resources.getQuantityString(R.plurals.hours_ago, hours.toInt(), hours.toInt())
        }
        seconds < 604800 -> {
            val days = seconds / 86400f
            context.resources.getQuantityString(R.plurals.days_ago, days.toInt(), days.toInt())
        }
        seconds < 2628000 -> {
            val weeks = seconds / 604800f
            context.resources.getQuantityString(R.plurals.weeks_ago, weeks.toInt(), weeks.toInt())
        }
        seconds < 31536000 -> {
            val months = seconds / 2628000f
            context.resources.getQuantityString(
                R.plurals.months_ago,
                months.toInt(),
                months.toInt()
            )
        }
        else -> {
            val years = seconds / 31536000f
            context.resources.getQuantityString(R.plurals.years_ago, years.toInt(), years.toInt())
        }
    }
}

/**
 * Extension function for MutableLiveData to notify observer when some
 * changes happends inside wrapper
 */
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}