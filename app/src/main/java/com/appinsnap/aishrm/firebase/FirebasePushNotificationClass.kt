package com.appinsnap.aishrm.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.appinsnap.aishrm.R
import com.appinsnap.aishrm.ui.activities.splashscreen.SplashScreen
import com.appinsnap.aishrm.util.LoggerGenratter
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebasePushNotificationClass : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object {
        var counter = 0
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        var type: String? = "Zindigi"
        var title: String? = "Notification"
        var body: String? = ""
        var caseId: String? = ""
        var isPushNotification: Boolean? = false
        try {
            if (remoteMessage.data.isNotEmpty()) {

                type = remoteMessage.data["Type"]
                body = remoteMessage.data["Body"]
                title = remoteMessage.data["Title"]
                caseId = remoteMessage.data["GlobalCaseId"]
                isPushNotification = remoteMessage.data["isPushNotification"].toBoolean()
                LoggerGenratter.getInstance().printLog("PUSH NOTIFICATION", "onMessageReceived: Title = $title")


            } else {
                LoggerGenratter.getInstance().printLog("PUSH NOTIFICATION", "onMessageReceived: Notification Count ->"+remoteMessage.notification)
                remoteMessage.notification?.let { notification ->
                    body = notification.body
                    title = notification.title

                }
            }

        } catch (exp: Exception) {

            LoggerGenratter.getInstance().printLog("PUSH NOTIFICATION",  "onMessageReceived: ${exp.message}")
        }
        RunNotification(title, body, type, caseId, isPushNotification)
    }

    private fun RunNotification(
        title: String?,
        description: String?,
        type: String?,
        caseId: String?,
        isPushNotification: Boolean?
    ) {
        val manager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "notification_channel_id"
        val channelName = "task_name"
        try {
            val intent = Intent(
                applicationContext,
                SplashScreen::class.java
            )
            // Here pass your activity where you want to redirect.
            intent.putExtra("isPushNotification", isPushNotification)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            var pendingIntent: PendingIntent? = null
            try {
                pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
                } else {
                    PendingIntent.getActivity(
                        this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                    )
                }
            } catch (exp: Exception) {
                LoggerGenratter.getInstance().printLog("PUSH NOTIFICATION",  exp.message)
            }


            /* pendingIntent = PendingIntent.getActivity(this, 0 */ /* Request code */ /*, intent,
                    PendingIntent.FLAG_ONE_SHOT);*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channel)
            }
            val builder = NotificationCompat.Builder(applicationContext, channelId)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.appicon)
            manager.notify(counter, builder.build())
        }
        catch (exp: Exception) {
            LoggerGenratter.getInstance().printLog("PUSH NOTIFICATION", exp.message)
        }
    }
}