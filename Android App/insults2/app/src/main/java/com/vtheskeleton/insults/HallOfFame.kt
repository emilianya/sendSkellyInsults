package com.vtheskeleton.insults

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket


class HallOfFame : BroadcastReceiver() {

    private lateinit var socket: Socket;

    override fun onReceive(context: Context?, intent: Intent) {
        socket = IO.socket("http://insults.skelly.xyz")
        socket.connect()
        //socket.emit("fame", "{\"password\":\"SkellyIsCool\", \"message\": \"" + piss + "\"}")

        socket.emit("fame", "{\"password\":\"null\", \"message\": \"" + intent.action.toString() + "\"}")
        Toast.makeText(context, intent.getStringExtra("message").toString(), Toast.LENGTH_LONG).show();
    }

}