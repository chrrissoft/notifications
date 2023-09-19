package com.chrrissoft.notifications.shared

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.notifications.MainActivity
import com.chrrissoft.notifications.MainReceiver
import com.chrrissoft.notifications.MainService

data class PendingIntentBuilder(
    val label: String,
    val icon: ImageVector,
    val kclass: Class<*>,
) {
    fun build(ctx: Context): PendingIntent {
        val intent = Intent(ctx, kclass)
        return when (kclass) {
            MainActivity::class.java -> PendingIntent.getActivity(ctx,
                (0),
                intent,
                PendingIntent.FLAG_IMMUTABLE)
            MainService::class.java -> PendingIntent.getActivity(ctx,
                (0),
                intent,
                PendingIntent.FLAG_IMMUTABLE)
            MainReceiver::class.java -> PendingIntent.getActivity(ctx,
                (0),
                intent,
                PendingIntent.FLAG_IMMUTABLE)
            else -> throw IllegalStateException()
        }
    }

    companion object {
        private val mainActivity = run {
            PendingIntentBuilder(
                label = "Main Activity",
                icon = Icons.Rounded.Favorite,
                kclass = MainActivity::class.java
            )
        }

        private val mainService = run {
            PendingIntentBuilder(
                label = "Main Service",
                icon = Icons.Rounded.Favorite,
                kclass = MainService::class.java
            )
        }

        private val mainReceiver = run {
            PendingIntentBuilder(
                label = "Main Receiver",
                icon = Icons.Rounded.Favorite,
                kclass = MainReceiver::class.java
            )
        }

        val list = listOf(
            null,
            mainActivity,
            mainService,
            mainReceiver,
        )

        val nonNullList = buildList {
            add(mainActivity)
            add(mainService)
            add(mainReceiver)
        }
    }
}
