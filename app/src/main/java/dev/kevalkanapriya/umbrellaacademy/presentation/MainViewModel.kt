package dev.kevalkanapriya.umbrellaacademy.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kevalkanapriya.util.sensorUtil.MeasurableSensor
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val accelerometerSensor: MeasurableSensor
) {



    init {
        accelerometerSensor.startListening()
        accelerometerSensor.setOnSensorValuesChangedListener { values ->


        }
    }
}