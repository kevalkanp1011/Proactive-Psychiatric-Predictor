package dev.kevalkanapriya.umbrellaacademy.data.remote.dto

sealed interface ApiResponse<T> {

    class Success<T>(data: T): ApiResponse<T>

    class Failed<T>(message: String?, data: T? = null): ApiResponse<T>
    class Loading<T>: ApiResponse<T>
}