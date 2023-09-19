package com.chrrissoft.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationChannelGroupCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@RequiresApi(Build.VERSION_CODES.O)
fun notification(ctx: Context) {
    Notification.Builder(ctx, "")

    NotificationCompat.Builder(ctx, "")
}

@RequiresApi(Build.VERSION_CODES.O)
fun channel(ctx: Context) {
    NotificationChannel("", "", NotificationManager.IMPORTANCE_HIGH)

    NotificationChannelCompat.Builder("", NotificationManager.IMPORTANCE_MIN)
}

@RequiresApi(Build.VERSION_CODES.O)
fun group(ctx: Context) {
    NotificationChannelGroup("", "")

    NotificationChannelGroupCompat.Builder("")
        .setDescription("")
        .setName("")
        .build()
}

fun manager(ctx: Context) {
    ctx.getSystemService(NotificationManager::class.java)

    ctx.getSystemService(NotificationManagerCompat::class.java)
}
