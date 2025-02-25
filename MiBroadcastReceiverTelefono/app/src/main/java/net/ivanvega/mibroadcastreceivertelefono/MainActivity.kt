package net.ivanvega.mibroadcastreceivertelefono

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var stateSwitch: Boolean = false
    val br: BroadcastReceiver = MyBroadcastReceiver()
    lateinit var btnSwitch: Switch


    val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
        addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        //addAction("android.intent.action.PHONE_STATE")
        //addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stateSwitch = isMyServiceRunning(ServicePhoneState::class.java)
        btnSwitch = findViewById(R.id.swPhoneState)
        btnSwitch.isChecked = stateSwitch
        btnSwitch.setOnClickListener { view: View? ->
            stateSwitch = btnSwitch.isChecked
            if (stateSwitch) {
                val callService = Intent(this, ServicePhoneState::class.java)
                try {
                    startService(callService)
                    Log.d(packageName, "onClick: starting service")
                } catch (ex: Exception) {
                    Log.d(packageName, ex.toString())
                }
            } else {
                stopService(Intent(this, ServicePhoneState::class.java))
                Log.d(packageName, "onClick: stoping service")
            }
        }

        registerReceiver(br, filter)
    }
}