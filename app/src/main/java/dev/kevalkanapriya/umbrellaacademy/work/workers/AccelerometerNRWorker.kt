package dev.kevalkanapriya.umbrellaacademy.work.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.kevalkanapriya.umbrellaacademy.data.remote.ApiService
import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.ApiResponse
import java.io.File

class AccelerometerNRWorker constructor(
    private val apiService: ApiService,
    private val context: Context,
    workerParams: WorkerParameters,

): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {

        val filePath = "${context.filesDir}/accelerometer_data.csv"
        val result = apiService.sendAccelerometerData(
            File(filePath)
        )

        return when(result) {

            is ApiResponse.Success -> {
                Log.d("AccelerometerNRWorker", "data: $result")
                Result.success()
            }

            is ApiResponse.Failed -> {
                Result.failure()
            }

            else -> {
                Result.failure()
            }
        }


    }
}