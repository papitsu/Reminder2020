package com.example.reminder2020

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //To change body of created functions use File | Settings | File Templates.
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val geofencingTransition = geofencingEvent.geofenceTransition

        if (geofencingTransition == Geofence.GEOFENCE_TRANSITION_ENTER
            || geofencingTransition == Geofence.GEOFENCE_TRANSITION_DWELL
        ) {
            var uid = intent!!.getIntExtra("uid", 0)
            var text = intent.getStringExtra("message")

            MainActivity.showNotification(context!!, text)

        }
    }
}