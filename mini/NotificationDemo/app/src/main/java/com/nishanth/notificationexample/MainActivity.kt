package com.nishanth.notificationexample

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

class MainActivity : AppCompatActivity() {
    companion object{
        private val CHANNEL_1_ID = "channel1"
        private val CHANNEL_2_ID = "channel2"
        val messages=mutableListOf<Message>()
        fun sendNotificationOnChannel1(context: Context) {
            val contentIntent = Intent(context, MainActivity::class.java)
            val contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, 0)
            val remoteInput: RemoteInput = RemoteInput.Builder("key_input_reply")
                    .setLabel("Leave your reply here...")
                    .build()
            val replyIntent = Intent(context, BR::class.java)
            val replyPendingIntent = PendingIntent.getBroadcast(context, 0, replyIntent, 0)
            val replyAction = NotificationCompat.Action.Builder(R.drawable.ic_baseline_send_24, "Reply", replyPendingIntent)
                    .addRemoteInput(remoteInput).build()
            val messagingStyle=NotificationCompat.MessagingStyle("You")
                    .setConversationTitle("Group Chat")
            for(chatMessage in messages){
                messagingStyle.addMessage(NotificationCompat.MessagingStyle.Message(chatMessage.text, chatMessage.timestamp, chatMessage.sender))
            }
            val notification = NotificationCompat.Builder(context, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(contentPendingIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .setColor(Color.GREEN)
                    .setStyle(messagingStyle)
                    .addAction(replyAction)
                    .build()

            NotificationManagerCompat.from(context).notify(0, notification)
        }
    }
    lateinit var notificationManager: NotificationManagerCompat
    lateinit var editTextTitle: EditText
    lateinit var editTextMsg: EditText
    lateinit var sendOn1: Button
    lateinit var sendOn2: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = NotificationManagerCompat.from(this)
        editTextTitle = findViewById(R.id.edit_text_title)
        editTextMsg = findViewById(R.id.edit_text_message)
        sendOn1 = findViewById(R.id.sendOn1)
        sendOn2 = findViewById(R.id.sendOn2)
        messages.add(Message("Good Morning", "Karthikeya"))
        messages.add(Message("Good Morning! How are you all?", null))
        messages.add(Message("What'sUp guys", "Girish"))

    }
    fun sendOnChannel2(view: View) {
        val title = editTextTitle.text.toString()
        val msg = editTextMsg.text.toString()
        val notification = NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setContentText(msg)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.InboxStyle()
                        .addLine("This is line 1")
                        .addLine("This is line 2")
                        .addLine("This is line 3")
                        .addLine("This is line 4")
                        .setBigContentTitle("Big Content Title")
                        .setSummaryText("Summary Text"))
                .build()
        notificationManager.notify(1, notification)
    }

    fun sendOnChannel1(view: View) {
        sendNotificationOnChannel1(this)
    }
}