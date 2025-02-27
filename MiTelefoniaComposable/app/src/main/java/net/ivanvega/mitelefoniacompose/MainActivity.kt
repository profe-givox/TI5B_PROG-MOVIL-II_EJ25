package net.ivanvega.mitelefoniacompose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import net.ivanvega.mitelefoniacompose.ui.theme.MiTelefoniaComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiTelefoniaComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                    HomeScreen()
                }
            }
        }
    }
}

@Composable
fun SystemBroadcastReceiver(
    systemAction: String,
    onSystemEvent: (intent: Intent?) -> Unit
) {
    // Grab the current context in this part of the UI tree
    val context = LocalContext.current

    // Safely use the latest onSystemEvent lambda passed to the function
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    // If either context or systemAction changes, unregister and register again
    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

@Composable
fun HomeScreen() {

    SystemBroadcastReceiver(Telephony.Sms.Intents.SMS_RECEIVED_ACTION) { intent ->
        var strMensaje = ""
        val isCharging = /* Get from batteryStatus ... */ true
        val bndSMS: Bundle? = intent?.getExtras()
        val pdus = bndSMS?.get("pdus") as Array<Any>?
        val smms: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pdus!!.size)
        for (i in smms.indices) {
            smms[i] = SmsMessage.createFromPdu(pdus!![i] as ByteArray)
            strMensaje +="${"Mensaje: " + smms[i]?.getOriginatingAddress()}\n" +
                    "${smms[i]?.getMessageBody().toString()}"


        }
        Log.d("MiBroadcastEnEjecucion", strMensaje)
        /* Do something if the device is charging */

    }

    /* Rest of the HomeScreen */
}

@Composable
fun Greeting(name: String, viewModel: ScreenViewModel = ScreenViewModel(),     modifier: Modifier = Modifier) {
    viewModel.sendSMS()
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MiTelefoniaComposeTheme {
        Greeting("Android")
    }
}