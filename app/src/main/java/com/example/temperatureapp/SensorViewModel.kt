package com.example.temperatureapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class SensorViewModel : ViewModel() {

    private val _temperature = MutableStateFlow(0f)
    val temperature: StateFlow<Float> get() = _temperature

    private val client = OkHttpClient()

    fun startSensor(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val temp = getTemperature()
                _temperature.value = temp
                sendTemperatureToApi(deviceId, temp)
                delay(10000) // Wait for 10 seconds
            }
        }
    }

    private external fun getTemperature(): Float

    private fun sendTemperatureToApi(deviceId: String, temperature: Float) {
        val jsonObject = JSONObject().apply {
            put("deviceId", deviceId)
            put("tempValue", temperature)
        }

        val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("http://10.0.2.2:5297/api/temperature") // Replace with actual endpoint
            .post(body)
            .build()

        client.newCall(request).execute()
    }

    init {
        System.loadLibrary("temperatureapp") // Load the C++ library
    }
}
