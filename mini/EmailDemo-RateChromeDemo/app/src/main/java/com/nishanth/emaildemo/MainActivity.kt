package com.nishanth.emaildemo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var textTo: EditText
    lateinit var textSubject: EditText
    lateinit var textMessage: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textTo = findViewById(R.id.text_to)
        textSubject = findViewById(R.id.text_subject)
        textMessage = findViewById(R.id.text_message)
    }
    fun rateChrome(view:View){
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+"com.android.chrome"))
            startActivity(intent)
        }catch(e:ActivityNotFoundException){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+"com.android.chrome"))
            startActivity(intent)
        }
    }
    fun sendDetails(view: View) {
        sendEmail()
    }

    private fun sendEmail() {
        val emailsString:String =textTo.text.toString()
        val recipients=emailsString.split(",").toTypedArray()
        val intent= Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT,textSubject.text.toString())
        intent.putExtra(Intent.EXTRA_TEXT, textMessage.text.toString())
        intent.setType("message/rfc822")
        startActivity(Intent.createChooser(intent, "Send with..."))

    }

}