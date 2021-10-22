package com.hornet.hornetinbox.common.util.date

import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT = "E, dd MMM yyyy HH:mm:ss"

object DateUtil {
    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(DATE_FORMAT)
        return format.format(date)
    }

    fun convertLongToDate(time: Long): Date {
        return Date(time)
    }
}