package com.vtheskeleton.insults

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URLDecoder

class ListeningService : Service() {
    private lateinit var socket : Socket;
    var mHandler = Handler()
    var idIter = 0

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onCreate()
        createNotificationChannel()
        Log.v("LISTENER SERVICE", "start Called")
        socket = IO.socket("http://insults.skelly.xyz/")
        //socket = IO.socket("http://84.204.100.113:8004")
        socket.connect()
        socket.on("sendnotif") { args -> sendNotif(URLDecoder.decode(args[0].toString()))}

        return Service.START_STICKY

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Send Skelly Insults"
            val descriptionText = "SendSkellyInsults notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Send Skelly Insults", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotif(args : String) {
        mHandler.post(Runnable {
            Toast.makeText(
                this@ListeningService,
                args.toString(),
                Toast.LENGTH_LONG
            ).show()
        })
        val fameIntent = Intent(this, HallOfFame::class.java).apply {
        action = args
        putExtra("message", "args")
    }
        val famePendingIntent: PendingIntent = PendingIntent.getBroadcast(this, idIter, fameIntent, 0)

        var builder = NotificationCompat.Builder(this, "Send Skelly Insults")
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New Insult Received!")
            .setContentText(args)
            .addAction(R.drawable.notification_icon, getString(R.string.hallOfFame),
                famePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(idIter, builder.build())
            idIter++
        }
    }
}