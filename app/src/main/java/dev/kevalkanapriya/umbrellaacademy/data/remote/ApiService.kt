package dev.kevalkanapriya.umbrellaacademy.data.remote

import dev.kevalkanapriya.umbrellaacademy.data.remote.dto.ApiResponse
import io.ktor.client.HttpClient
import java.io.File

typealias Response = ApiResponse<String>

interface ApiService {

    suspend fun sendAccelerometerData(file: File): Response
    suspend fun sendKeyLoggerData(file: File): Response

    suspend fun sendQnAData(file: File): Response


    companion object {
        fun create(client: HttpClient): ApiService {
            return ApiServiceImpl(client)
        }
    }

}