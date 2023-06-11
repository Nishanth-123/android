package com.nishanth.notificationexample

import androidx.core.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT

class BR :BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle=RemoteInput.getResultsFromIntent(intent)
        bundle?.let {
            val inputText=bundle.getCharSequence("key_input_reply")
            val message=Message(inputText.toString(), null)
            MainActivity.messages.add(message)
            MainActivity.sendNotificationOnChannel1(context!!)
        }
    }
}