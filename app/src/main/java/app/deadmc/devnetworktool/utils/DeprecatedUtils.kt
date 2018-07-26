package app.deadmc.devnetworktool.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import app.deadmc.devnetworktool.R
import app.deadmc.devnetworktool.ui.presentation.activities.MainActivity

fun startServiceForeground(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent)
    } else {
        context.startService(intent)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getNotificationChannel(context: Context, description: String): String {
    val id = "1613"
    val name = "DevNetworkTool"
    val importance = android.app.NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(id, name, importance)
    channel.description = description
    channel.enableLights(true)
    channel.lightColor = Color.RED
    channel.enableVibration(true)
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
    notificationManager.createNotificationChannel(channel)
    return id
}

fun getNotificationCompatBuilder(context: Context, description: String): NotificationCompat.Builder {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return NotificationCompat.Builder(context, getNotificationChannel(context, description))
    } else {
        return NotificationCompat.Builder(context)
    }
}

fun getNotification(context: Context, description: String):Notification {
    val notificationIntent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0,
            notificationIntent, 0)
    val builder = getNotificationCompatBuilder(context,description)
    if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        return builder
                .setSmallIcon(R.drawable.main_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.main_icon_w))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(description)
                .setContentIntent(pendingIntent).build()
    } else {
        return builder
                .setSmallIcon(R.drawable.main_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.main_icon))
                .setContentTitle(context.getString(R.string.app_name))
                .setColor(0x00ffffff)
                .setContentText(description)
                .setContentIntent(pendingIntent).build()
    }
}


