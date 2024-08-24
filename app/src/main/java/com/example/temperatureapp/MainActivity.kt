package com.example.temperatureapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.temperatureapp.ui.theme.TemperatureAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureAppTheme {
                MainScreen(
                    viewModel = viewModel,
                    onDeviceIdChecked = { isValid, deviceId ->
                        if (isValid) {
                            val intent = Intent(this, SensorActivity::class.java)
                            intent.putExtra("DEVICE_ID", deviceId)
                            startActivity(intent)
                        }
                        else {
                            // Show a Toast message if Device ID is not valid
                            Toast.makeText(this, "Invalid Device ID. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onDeviceIdChecked: (Boolean, String) -> Unit
) {
    var deviceId by remember { mutableStateOf("") }
    val isDeviceIdValid by viewModel.isDeviceIdValid.observeAsState(false)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = deviceId,
            onValueChange = { deviceId = it },
            label = { Text("Enter Device ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (deviceId.isNotEmpty()) {
                    viewModel.checkDeviceId(deviceId)
                } else {
                    Toast.makeText(context, "Please enter a Device ID", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Check Device ID")
        }
        if (isDeviceIdValid) {
            onDeviceIdChecked(isDeviceIdValid, deviceId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TemperatureAppTheme {
        MainScreen(viewModel = MainViewModel(), onDeviceIdChecked = { _, _ -> })
    }
}
