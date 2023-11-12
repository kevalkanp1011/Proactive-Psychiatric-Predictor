package dev.kevalkanapriya.umbrellaacademy.work

import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkManager
import dev.kevalkanapriya.umbrellaacademy.work.factory.MyWorkerFactory
import org.koin.core.KoinApplication

fun KoinApplication.myWorkManagerFactory() {
    createWorkManagerFactory()
}

private fun KoinApplication.createWorkManagerFactory() {


    val factory2 = DelegatingWorkerFactory()
        .apply {
            addFactory(MyWorkerFactory())
        }


    val conf = Configuration.Builder()
        .setWorkerFactory(factory2)

        .build()

    WorkManager.initialize(koin.get(), conf)
}