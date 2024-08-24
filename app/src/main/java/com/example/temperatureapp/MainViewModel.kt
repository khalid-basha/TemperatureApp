package com.example.temperatureapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class MainViewModel : ViewModel() {

    private val _isDeviceIdValid = MutableLiveData<Boolean>()
    val isDeviceIdValid: LiveData<Boolean> get() = _isDeviceIdValid

    private val client = OkHttpClient()

    fun checkDeviceId(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("http://10.0.2.2:5297/api/device/$deviceId") // Ensure this URL is correct and reachable
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    _isDeviceIdValid.postValue(true)
                } else {
                    // Log or handle different response codes if needed
                    handleError(response.code)
                    _isDeviceIdValid.postValue(false)
                }
            } catch (e: Exception) {
                // Handle network errors or exceptions
                handleError(e)
                _isDeviceIdValid.postValue(false)
            }
        }
    }

    private fun handleError(code: Int) {
        // Log error or notify user
        Log.e("DeviceIdCheck", "Request failed with code: $code")
    }

    private fun handleError(exception: Exception) {
        // Log or handle the exception
        Log.e("DeviceIdCheck", "Request failed with exception: ${exception.message}")
    }
}
