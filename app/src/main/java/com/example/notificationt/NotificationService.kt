package com.example.notificationt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
import me.leolin.shortcutbadger.ShortcutBadger

class NotificationService : FirebaseMessagingService() {

    private lateinit var notificationManager: NotificationManager

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "RemoteMessage: ${remoteMessage.data.get("data")}")
        super.onMessageReceived(remoteMessage)
        var data = remoteMessage.data
        var body: String = data.get("body").toString()
        var title: String = data.get("title").toString()
        var group: String = data.get("group_key").toString()
        var badge: Int = data.get("badge").toString().toInt()
        // notification sample
        // pushNotification(title, body)
        // Notification group
        pushNotificationGroup(title, body, group, badge)
    }

    private fun pushNotificationGroup(title: String, body: String, group: String, badge: Int) {
        var intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(getString(R.string.message_extra), body)
        intent.setAction(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var channelId = getString(R.string.default_notification_channel_id)

        val newNotification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setGroup(group)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        var groupNotificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setChannelId(channelId)
            .setContentIntent(pendingIntent)
            .setGroup(group)
            .setGroupSummary(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        var notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_HIGH)
            channel.description = getString(R.string.default_notification_channel_description)
            // channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }
        // setup badge
        setBadge(badge)
        // push notification (show banner)
        NotificationManagerCompat.from(this).apply {
            notify(System.currentTimeMillis().toInt() ,newNotification)
            var index = group.indexOf("_")
            var id = group.substring(index + 1, group.length).toInt()
            Log.d(TAG, "notificationId: ${id}")
            notify(id, groupNotificationBuilder)
        }
    }

    private fun pushNotification(title: String, body: String) {
        Log.d(TAG, "title: ${title}, body: ${body}")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setupNotificationChannels()
        }
        var intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var channelId = getString(R.string.default_notification_channel_id)
        var notificationBuildConfig = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setChannelId(channelId)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroupSummary(true)
            .build()
        var notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt(60000)
        notificationManager.notify(notificationId /* ID of notification */, notificationBuildConfig)
    }

    private fun setupNotificationChannels() {
        Log.d(TAG, "${android.os.Build.VERSION.SDK_INT} >= ${android.os.Build.VERSION_CODES.O}")
        var adminChannelName = getString(R.string.default_notification_channel_name)
        var adminChanelDescriptor = getString(R.string.default_notification_channel_description)
        var adminChannelId = getString(R.string.default_notification_channel_id)

        var adminChannel: NotificationChannel
        adminChannel = NotificationChannel(adminChannelId, adminChannelName, NotificationManager.IMPORTANCE_LOW)
        adminChannel.description = adminChanelDescriptor
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager.createNotificationChannel(adminChannel)
    }

    private fun setBadge(badge: Int) {
        Log.d(TAG, "Badge: ${badge}")
        val success = ShortcutBadger.applyCount(baseContext, badge)
        Log.d(TAG, "Set Count = ${badge}, Success=${success}")
    }

    private fun action() {

    }

    companion object {
        private const val TAG = "NotificationService"
    }
}