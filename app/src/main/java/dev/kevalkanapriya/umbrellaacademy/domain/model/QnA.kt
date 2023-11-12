package dev.kevalkanapriya.umbrellaacademy.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class QnA(
    val no: Int,
    val question: String
)
