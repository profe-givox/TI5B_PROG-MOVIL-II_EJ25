package net.ivanvega.mibroadcastreceivertelefono

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast


private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            when(intent.action){
                Intent.ACTION_BOOT_COMPLETED -> {  Toast.makeText(context,
                    "SE boot el sistema", Toast.LENGTH_LONG).show()
                }
                Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> {
                    val bndSMS = intent.extras
                    val pdus = bndSMS!!["pdus"] as Array<Any>?
                    val smms: Array<SmsMessage?> = arrayOfNulls<SmsMessage>(pdus!!.size)
                    var strMensaje = ""
                    for (i in 0 until smms.size) {
                        smms[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        strMensaje = "Mensaje: " + smms[i]!!.getOriginatingAddress() + "\n" +
                                smms[i]!!.getMessageBody().toString();
                    }
                    Log.d("MiBroadcast", strMensaje);
                    Toast.makeText(context, strMensaje, Toast.LENGTH_LONG).show()
                }

                else -> {

                }

            }

            StringBuilder().apply {
                append("Action: ${intent.action}\n")
                append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
                toString().also { log ->
                    Log.d(TAG, log)
                    Toast.makeText(context, log, Toast.LENGTH_LONG).show()
                }
            }
        }
    }