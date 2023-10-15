package dev.kevalkanapriya.umbrellaacademy.domain.model

import kotlinx.serialization.SerialInfo
import kotlinx.serialization.Serializable

@Serializable
data class KeyLoggerData(
    val timestamp: Long,
    val package_name: String,
    val text: String
)
