package dev.kevalkanapriya.umbrellaacademy

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dev.kevalkanapriya.umbrellaacademy.di.appModule
import dev.kevalkanapriya.umbrellaacademy.notification.NotificationConstant
import dev.kevalkanapriya.umbrellaacademy.work.myWorkManagerFactory
import dev.kevalkanapriya.util.Constant.FOREGROUND_CHANNEL_ID
import dev.kevalkanapriya.util.Constant.FOREGROUND_CHANNEL_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin


class UmbrellaAcademyApp: Application(), KoinComponent {



    override fun onCreate() {
        super.onCreate()



        startKoin {
            androidLogger()
            androidContext(this@UmbrellaAcademyApp)
            modules(appModule)
            myWorkManagerFactory()
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createForegroundNotificationChannel(get())
            createReminderNotificationChannel(get())
        }

    }




    //Channel for foreground notification

    private fun createForegroundNotificationChannel(notificationManager: NotificationManager) {
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                FOREGROUND_CHANNEL_ID,
                FOREGROUND_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun createReminderNotificationChannel(notificationManager: NotificationManager) {
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                NotificationConstant.ReminderNotificationChannelId,
                NotificationConstant.ReminderChannelName,
                NotificationManager.IMPORTANCE_HIGH
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel)
        }
    }
}





