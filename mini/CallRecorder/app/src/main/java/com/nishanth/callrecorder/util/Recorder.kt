package com.nishanth.callrecorder.util

import com.nishanth.callrecorder.MainActivity

import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.util.*


class Recorder {

    private var fileName:String?=null
    private var recorder:MediaRecorder?=null

    fun startRecording() {
        val uuid: String = UUID.randomUUID().toString()
        fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath().toString() + "/" + uuid + ".3gp"
        recorder = MediaRecorder()
        recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder!!.setOutputFile(fileName)
        recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder!!.prepare()
        } catch (e: IOException) {

        }
        recorder!!.start()
    }

    fun stopRecording() {
        if (recorder != null) {
            recorder!!.release()
            recorder = null
        }
    }
}