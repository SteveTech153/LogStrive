package com.example.logstrive.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

object Helper {
    fun convertDateToLong(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
    fun getYesterdayDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }
    fun convertLongToDate(timeInMillis: Long): Date {
        return Date(timeInMillis)
    }
    fun formatDate(date: Date): String {
        val formatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        return formatter.format(date)
    }
    fun minutesToTimeString(minutes: Int): String {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return String.format("%02d:%02d:00", hours, remainingMinutes)
    }

    fun calculateDaysBetween(startDateInMillis: Long, endDateInMillis: Long): Int {
        val start = Date(startDateInMillis)
        val end = Date(endDateInMillis)

        val diffInMillis = end.time - start.time

        return TimeUnit.MILLISECONDS.toDays(diffInMillis).toInt()
    }
}