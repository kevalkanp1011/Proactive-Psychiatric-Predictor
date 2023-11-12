package dev.kevalkanapriya.umbrellaacademy.work

import java.util.Calendar


fun initialDelayInMillis(
    hour: Int,
    minute: Int,
): Long {
    val calendar: Calendar = Calendar.getInstance()
    val nowMillis: Long = calendar.timeInMillis
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    if (calendar.before(Calendar.getInstance())) {
        calendar.add(Calendar.DATE, 1)
    }

    return calendar.timeInMillis - nowMillis
}