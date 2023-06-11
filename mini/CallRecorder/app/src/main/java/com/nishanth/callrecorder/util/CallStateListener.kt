package com.nishanth.callrecorder.util

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log


class CallStateListener(val isCallMadeByThisApp:Boolean=false): PhoneStateListener() {

    private var recorder: Recorder=Recorder()
    override fun onCallStateChanged(state: Int, phoneNumber: String?) {
        super.onCallStateChanged(state, phoneNumber)

        if(state==TelephonyManager.CALL_STATE_RINGING && isCallMadeByThisApp){
            Log.d("before call", "Ok succeeded")
        }
        if(state==TelephonyManager.CALL_STATE_OFFHOOK && isCallMadeByThisApp){

            Log.d("during call", "Ok succeeded")
            //recorder= Recorder()
            recorder.startRecording()
        }
        if(state==TelephonyManager.CALL_STATE_IDLE && isCallMadeByThisApp){
            Log.d("after call", "Ok succeeded")
            recorder.stopRecording()
        }
    }
}