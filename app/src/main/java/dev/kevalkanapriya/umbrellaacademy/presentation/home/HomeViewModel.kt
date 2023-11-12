package dev.kevalkanapriya.umbrellaacademy.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(

): ViewModel() {

    private val channel = Channel<HomeEvent>()
    val channelFlow = channel.receiveAsFlow()

    var isEmptyCard = mutableStateOf(false)
        private set

    fun setEmptyCardState() {
        isEmptyCard.value = !isEmptyCard.value
    }

    fun showApiCall() {
        viewModelScope.launch {
            channel.send(HomeEvent.MakeApiCall)
        }
    }
}