package com.vtheskeleton.insults

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_DEFAULT
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.vtheskeleton.insults.R.layout
import io.socket.client.IO
import io.socket.client.Socket


class MainActivity : AppCompatActivity() {
    var idIter = 0
    //private lateinit var mSocket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "VITTU SAATANA")
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        createNotificationChannel()
        startService(Intent(this, ListeningService::class.java))
        Log.v("MAINACTIVITY", "Listener should now be started!")

    }
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Send Skelly Insults TEST"
            val descriptionText = "SendSkellyInsults TEST notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Send Skelly Insults TEST", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun testNotification(view: View) {

        Log.e("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "VITTU SAATANA")
        Toast.makeText(this@MainActivity, "TEST Notification Sent!", Toast.LENGTH_SHORT).show();

        val fameIntent = Intent(this, HallOfFame::class.java).apply {
            action = "fame"
            putExtra("notid", idIter)
        }
        val famePendingIntent: PendingIntent = PendingIntent.getBroadcast(this, idIter, fameIntent, 0)

        var builder = NotificationCompat.Builder(this, "Send Skelly Insults TEST")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New TEST Insult Received!")
            .setContentText("wee woo you are poo (this is a test!!)")
            .addAction(R.drawable.notification_icon, getString(R.string.hallOfFame),
                famePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(idIter, builder.build())
            idIter++
        }
    }
}