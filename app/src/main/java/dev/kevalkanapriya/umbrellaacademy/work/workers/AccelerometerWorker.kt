package dev.kevalkanapriya.umbrellaacademy.work.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import dev.kevalkanapriya.util.StoreDataToCSV
import dev.kevalkanapriya.util.sensorUtil.MeasurableSensor
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.util.Calendar


class AccelerometerWorker constructor(
    context: Context,
    workerParams: WorkerParameters,
    private val accelerometerSensor: MeasurableSensor
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {
        accelerometerSensor.startListening()
        accelerometerSensor.setOnSensorValuesChangedListener { values ->
            val x = values[0]
            val y = values[1]
            val z = values[2]

            Log.d("AccelerometerWorker", "x: $x, y: $y, z: $z")

            StoreDataToCSV(
                data = listOf(
                    listOf("${Calendar.getInstance().time}", "$x", "$y", "$z"),
                ),
                context = applicationContext,
                fileName = "accelerometer_data.csv"
            )
        }
        delay(100)

        Log.d("AccelerometerWorker", "stop Listening Called")
        accelerometerSensor.stopListening()
        return Result.success()
    }



}