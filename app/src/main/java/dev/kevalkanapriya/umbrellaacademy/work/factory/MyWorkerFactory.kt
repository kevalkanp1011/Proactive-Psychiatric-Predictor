package dev.kevalkanapriya.umbrellaacademy.work.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dev.kevalkanapriya.umbrellaacademy.work.workers.AccelerometerNRWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.AccelerometerWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.KeyLoggerNRWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.QnANRWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.QnAReminderWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MyWorkerFactory: WorkerFactory(), KoinComponent {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName) {


            QnAReminderWorker::class.java.name -> QnAReminderWorker(
                get(),
                appContext,
                workerParameters
            )

            AccelerometerWorker::class.java.name -> AccelerometerWorker(
                appContext,
                workerParameters,
                get()
            )

            AccelerometerNRWorker::class.java.name -> AccelerometerNRWorker(
                get(),
                appContext,
                workerParameters
            )

            KeyLoggerNRWorker::class.java.name -> KeyLoggerNRWorker(
                get(),
                appContext,
                workerParameters
            )

            QnANRWorker::class.java.name -> QnANRWorker(
                get(),
                appContext,
                workerParameters
            )

            else -> {
                null
            }
        }
    }




}