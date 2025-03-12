package net.ivanvega.missensoresconcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.ricknout.composesensors.accelerometer.isAccelerometerSensorAvailable
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState
import net.ivanvega.missensoresconcompose.ui.theme.MIsSensoresConComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MIsSensoresConComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // Check if accelerometer sensor is available
    val available = isAccelerometerSensorAvailable()

    // Remember accelerometer sensor value as State that updates as SensorEvents arrive
    val sensorValue by rememberAccelerometerSensorValueAsState()

    // Accelerometer sensor values. Also available: sensorValue.timestamp, sensorValue.accuracy
    val (x, y, z) = sensorValue.value

    if (available) {
        // Accelerometer sensor is available
        Canvas(modifier= Modifier.fillMaxSize()) {

        }
    } else {
        // Accelerometer sensor is not available
    }
    Text(
        text = "Hello $name!",
        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MIsSensoresConComposeTheme {
        Greeting("Android")
    }
}