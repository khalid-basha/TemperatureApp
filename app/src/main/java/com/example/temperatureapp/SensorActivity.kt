package com.example.temperatureapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.temperatureapp.ui.theme.TemperatureAppTheme

class SensorActivity : ComponentActivity() {

    private val viewModel: SensorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deviceId = intent.getStringExtra("DEVICE_ID") ?: ""

        setContent {
            TemperatureAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(
                        modifier = Modifier.padding(innerPadding),
                        deviceId = deviceId,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun SensorScreen(
    modifier: Modifier = Modifier,
    deviceId: String,
    viewModel: SensorViewModel
) {
    val temperature by viewModel.temperature.collectAsState(initial = 0f)

    LaunchedEffect(deviceId) {
        viewModel.startSensor(deviceId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Current Temperature: ${String.format("%.2f", temperature)} °C", modifier = Modifier.fillMaxWidth())
    }
}

@Preview(showBackground = true)
@Composable
fun SensorScreenPreview() {
    TemperatureAppTheme {
        SensorScreen(deviceId = "12345", viewModel = SensorViewModel())
    }
}
