package com.example.temperatureapp

class TemperatureSensor {
    external fun getTemperature(): Float

    companion object {
        init {
            System.loadLibrary("temperatureapp")
        }
    }
}