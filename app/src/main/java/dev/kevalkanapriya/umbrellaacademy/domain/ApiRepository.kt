package dev.kevalkanapriya.umbrellaacademy.domain

import dev.kevalkanapriya.umbrellaacademy.data.remote.ApiService
import dev.kevalkanapriya.umbrellaacademy.data.remote.Response
import java.io.File

class ApiRepository(
    private val apiService: ApiService
) {

    suspend fun sendAccelerometerData(file: File): Response {
        return apiService.sendAccelerometerData(file)
    }


    suspend fun sendKeyLoggerData(file: File): Response {
        return apiService.sendKeyLoggerData(file)
    }

    suspend fun sendQnAData(file: File): Response {
        return apiService.sendQnAData(file)
    }
}