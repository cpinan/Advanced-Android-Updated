package com.carlospinan.androidfirebasemessagingapp.data.repository.network

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.carlospinan.androidfirebasemessagingapp.R
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.Squawk
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.SquawkDatabase
import com.carlospinan.androidfirebasemessagingapp.ui.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

private const val NOTIFICATION_MAX_CHARACTERS = 30

class SquawkFirebaseMessageService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FBASE", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //  sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            // Send a notification that you got a new message
            sendNotification(data)
            insertSquawk(data)
        }
    }

    /**
     * Inserts a single squawk into the database;
     *
     * @param data Map which has the message data in it
     */
    private fun insertSquawk(data: Map<String, String>) {
        val squawk = Squawk(
            id = 0,
            author = data["author"] ?: "",
            authorKey = data["authorKey"] ?: "",
            message = data["message"] ?: "",
            date = data["date"]?.toLong() ?: System.currentTimeMillis()
        )
        SquawkDatabase.getInstance(applicationContext).squawkDao().insert(squawk)
    }

    /**
     * Create and show a simple notification containing the received FCM message
     *
     * @param data Map which has the message data in it
     */
    private fun sendNotification(data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Create the pending intent to launch the activity
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val author = data["author"]
        var message = data["message"]

        // If the message is longer than the max number of characters we want in our
        // notification, truncate it and add the unicode character for ellipsis
        if (message?.length!! > NOTIFICATION_MAX_CHARACTERS) {
            message = message?.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026"
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_duck)
            .setContentTitle(String.format(getString(R.string.notification_message), author))
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    /*
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1001, builder.build())
        }
     */

}