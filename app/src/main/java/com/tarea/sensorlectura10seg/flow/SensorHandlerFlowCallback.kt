package com.tarea.sensorlectura10seg.flow


import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// Clase que gestiona la interacción con un sensor específico
class SensorHandlerFlowCallback(
    private val sensorManager: SensorManager,
    private val sensor: Sensor
) {

    // Metodo que expone los datos del sensor como un Flow
    fun getDatosFlow(): Flow<FloatArray> = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { trySend(it.values) } // Emitimos los valores del sensor
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Metodo requerido pero no utilizado
            }
        }

        // Registramos el listener para comenzar a recibir datos
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        // Cerramos el flujo al finalizar
        awaitClose { sensorManager.unregisterListener(listener) }
    }
}
