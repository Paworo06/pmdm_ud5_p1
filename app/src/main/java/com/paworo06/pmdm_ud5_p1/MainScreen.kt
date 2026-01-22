package com.paworo06.pmdm_ud5_p1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen() {

    // Código para el Acelerómetro
    val context = LocalContext.current

    var x by remember { mutableStateOf(0f) }
    var y by remember { mutableStateOf(0f) }
    var z by remember { mutableStateOf(0f) }

    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val accelerometer = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    DisposableEffect(accelerometer) {
        if (accelerometer == null) {
            onDispose { }
        } else {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    x = event.values[0]
                    y = event.values[1]
                    z = event.values[2]
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // opcional
                }
            }

            sensorManager.registerListener(
                listener,
                accelerometer,
                SensorManager.SENSOR_DELAY_UI
            )

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    // Código para el sensor de luminosidad
    val context2 = LocalContext.current

    var lux by remember { mutableStateOf(0f) }

    val sensorManager2 = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    val lightSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    DisposableEffect(lightSensor) {
        if (lightSensor == null) {
            onDispose { }
        } else {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    lux = event.values[0]
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            sensorManager.registerListener(
                listener,
                lightSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    val etiqueta = when {
        lux < 10f -> "Oscuro"
        lux < 100f -> "Luz normal"
        else -> "Muy iluminado"
    }


    // Código para mostrar la información en la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Pantalla de Sensores", style = MaterialTheme.typography.titleLarge)


        Text("Acelerómetro", style = MaterialTheme.typography.titleMedium)
        if (accelerometer == null) {
            Text("Este dispositivo no tiene acelerómetro.")
        } else {
            Text("X: $x")
            Text("Y: $y")
            Text("Z: $z")
        }

        Text("Sensor de luz", style = MaterialTheme.typography.titleMedium)

        if (lightSensor == null) {
            Text("Este dispositivo no tiene sensor de luz.")
        } else {
            Text("Lux: $lux")
            Text("Estado: $etiqueta")
        }
    }
}
