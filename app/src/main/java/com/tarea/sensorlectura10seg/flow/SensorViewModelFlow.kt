package com.tarea.sensorlectura10seg.flow


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ViewModel encargado de procesar los datos de los sensores
class SensorViewModelFlow : ViewModel() {

    // Estados mutables para almacenar listas de valores
    var accelerometerData = mutableStateOf<List<String>>(emptyList())
    var temperatureData = mutableStateOf<List<String>>(emptyList())

    // Metodo para procesar los datos del acelerómetro
    fun startAccelerometer(sensorHandler: SensorHandlerFlowCallback) {
        viewModelScope.launch {
            sensorHandler.getDatosFlow()
                .filter { it[0] > 0.5 } // Filtra valores del eje X mayores a 0.5

                .map { datos -> "X: ${datos[0]}," +
                        " Y: ${datos[1]}," +
                        " Z: ${datos[2]}" } // Formatea los datos

                .sample(10000L) // Recoge valores cada 10 segundos

                .scan(emptyList<String>()) { acc, value -> acc + value } // Acumula valores

                .collect { lista -> accelerometerData.value = lista } // Actualiza la lista
        }
    }

    // Metodo para procesar los datos del sensor de temperatura
    fun startTemperature(sensorHandler: SensorHandlerFlowCallback) {
        viewModelScope.launch {
            sensorHandler.getDatosFlow()
                .filter { it[0] > 25 } // Filtra valores de temperatura mayores a 25°C
                .map { datos -> "Temperatura: ${datos[0]}°C" } // Formatea los datos
                .sample(10000L) // Recoge valores cada 10 segundos
                .scan(emptyList<String>()) { acc, value -> acc + value } // Acumula valores
                .collect { lista -> temperatureData.value = lista } // Actualiza la lista
        }
    }
}
