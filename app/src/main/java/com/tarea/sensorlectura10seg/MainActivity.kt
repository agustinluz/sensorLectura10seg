package com.tarea.sensorlectura10seg


import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.tarea.sensorlectura10seg.flow.SensorHandlerFlowCallback
import com.tarea.sensorlectura10seg.flow.SensorViewModelFlow
import com.tarea.sensorlectura10seg.ui.theme.SensorLectura10segTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager // Administrador de sensores
    private var accelerometer: Sensor? = null // Sensor de acelerómetro
    private var temperatureSensor: Sensor? = null // Sensor de temperatura

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita diseño edge-to-edge

        // Inicializamos el SensorManager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        // ViewModel para manejar los sensores
        val sensorViewModel = SensorViewModelFlow()

        // Configuramos los manejadores y comenzamos a recibir datos
        accelerometer?.let {
            val accelerometerHandler = SensorHandlerFlowCallback(sensorManager, it)
            sensorViewModel.startAccelerometer(accelerometerHandler)
        }

        temperatureSensor?.let {
            val temperatureHandler = SensorHandlerFlowCallback(sensorManager, it)
            sensorViewModel.startTemperature(temperatureHandler)
        }

        // Configuramos la interfaz de usuario
        setContent {
            SensorLectura10segTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(
                        modifier = Modifier.padding(innerPadding),
                        sensorViewModel = sensorViewModel
                    )
                }
            }
        }
    }
}




// Composable que muestra los datos de los sensores
@Composable
fun SensorScreen(modifier: Modifier = Modifier, sensorViewModel: SensorViewModelFlow) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        // Datos del acelerómetro
        item { Text("Datos del Acelerómetro:") }
        items(sensorViewModel.accelerometerData.value.size) { index ->
            Text(
                text = sensorViewModel.accelerometerData.value[index],
                modifier = Modifier.padding(8.dp)
            )
        }

        // Datos del sensor de temperatura
        item { Text("Datos de Temperatura:") }
        items(sensorViewModel.temperatureData.value.size) { index ->
            Text(
                text = sensorViewModel.temperatureData.value[index],
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
