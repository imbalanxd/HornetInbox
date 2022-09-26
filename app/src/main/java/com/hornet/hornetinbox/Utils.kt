package com.hornet.hornetinbox

import androidx.compose.ui.graphics.Color
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

object Utils {

    private const val EPOCH_YEAR = 1970
    private const val EPOCH_MONTH = Calendar.JANUARY

    fun generateBackgroundColor(): Color {
        return Color(
            red = Random().nextInt(256),
            green = Random().nextInt(256),
            blue = Random().nextInt(256),
        )
    }

    fun parseToLong(longValueInString: String): Long {
        return try {
            longValueInString.toLong()
        } catch (exception: Exception) {
            -1
        }
    }

    fun getRelativeTime(timeInLong: Long): String {
        val timeInMilliseconds = if (!isIncorrectEpochTime(timeInLong)) {
            timeInLong
        } else {
            timeInLong.times(1000L)
        }
        return PrettyTime(Locale.getDefault()).format(Date(timeInMilliseconds))
    }

    fun isIncorrectEpochTime(timeInLong: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInLong
        return EPOCH_YEAR <= calendar.get(Calendar.YEAR)
                && EPOCH_MONTH == calendar.get(Calendar.MONTH)
    }

}