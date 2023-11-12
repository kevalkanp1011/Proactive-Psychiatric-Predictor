package dev.kevalkanapriya.umbrellaacademy.notification

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import dev.kevalkanapriya.umbrellaacademy.R
import dev.kevalkanapriya.umbrellaacademy.notification.NotificationConstant.ReminderNotificationChannelId

fun reminderQnANotification(context: Context): Notification {

    return NotificationCompat
        .Builder(context, ReminderNotificationChannelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentText("you are assigned to the Questionnaire Queue")
        .setPriority(NotificationCompat.DEFAULT_ALL)
        .setCategory(Notification.CATEGORY_MESSAGE)
        .build()
}