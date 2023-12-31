package com.hotta.hoho.msg

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.CaseMap.Title
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hotta.hoho.R


class FireBaseService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        /* Log.d("msg123", message.notification?.body.toString())
         Log.d("msg123", message.notification?.title.toString())

         val title = message.notification?.title.toString()
         val body = message.notification?.body.toString()*/
        Log.d("msg123", message.notification?.body.toString())
        Log.d("msg123", message.notification?.title.toString())

        val title = message.data["title"].toString()
        val body = message.data["content"].toString()

        createNotificationChannel()
        sendNotification(title, body)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "Text"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Channel", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title: String, body: String) {
        var builder = NotificationCompat.Builder(this, "Test_Channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    baseContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(123, builder.build())
        }
    }
}