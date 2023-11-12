package dev.kevalkanapriya.umbrellaacademy.presentation.home

sealed interface HomeEvent {

    object MakeApiCall: HomeEvent
    data class ReceiveDataApi(val data: String): HomeEvent


}