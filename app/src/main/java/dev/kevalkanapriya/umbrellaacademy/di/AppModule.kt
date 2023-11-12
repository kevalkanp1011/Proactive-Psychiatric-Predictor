package dev.kevalkanapriya.umbrellaacademy.di

import android.app.NotificationManager
import android.content.Context
import dev.kevalkanapriya.umbrellaacademy.data.remote.ApiService
import dev.kevalkanapriya.umbrellaacademy.domain.ApiRepository
import dev.kevalkanapriya.umbrellaacademy.presentation.home.HomeViewModel
import dev.kevalkanapriya.umbrellaacademy.work.workers.AccelerometerNRWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.AccelerometerWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.KeyLoggerNRWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.QnAReminderWorker
import dev.kevalkanapriya.umbrellaacademy.work.workers.QnANRWorker
import dev.kevalkanapriya.util.ktorOKHTTPClient
import dev.kevalkanapriya.util.sensorUtil.AccelerometerSensor
import dev.kevalkanapriya.util.sensorUtil.MeasurableSensor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

    //System Service Manager
    single {
        androidContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    //SharedPref
    single {
        androidContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }


    //Sensor
    single<MeasurableSensor> { AccelerometerSensor(get()) }


    //ApiService
    single { ApiService.create(ktorOKHTTPClient) }

    //Repository
    singleOf(::ApiRepository)

    //viewModel
    viewModelOf(::HomeViewModel)

    //Worker
    workerOf(::AccelerometerWorker)
    workerOf(::QnAReminderWorker)
    workerOf(::KeyLoggerNRWorker)
    workerOf(::QnANRWorker)
    workerOf(::AccelerometerNRWorker)


}