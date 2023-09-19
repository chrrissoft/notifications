package com.chrrissoft.notifications.channels

import android.annotation.SuppressLint
import android.app.Notification.*
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager.*
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.*
import android.service.notification.NotificationListenerService.Ranking.VISIBILITY_NO_OVERRIDE
import androidx.annotation.RequiresApi
import com.chrrissoft.notifications.Util.resolveColor
import com.chrrissoft.notifications.channels.ChannelsState.BuilderState
import com.chrrissoft.notifications.channels.ChannelsState.BuilderState.Importance
import com.chrrissoft.notifications.channels.ChannelsState.BuilderState.Importance.*
import com.chrrissoft.notifications.shared.Sound
import com.chrrissoft.notifications.shared.Vibration
import com.chrrissoft.notifications.shared.Visibility
import com.chrrissoft.notifications.shared.Visibility.*

object Util {
    @SuppressLint("NewApi")
    @RequiresApi(O)
    fun NotificationChannel.copy(): BuilderState {
        return BuilderState(
            name = "${this.name}",
            group = Pair(this.group, ""),
            description = this.description,
            importance = this.importance.resolveImportance,
            color = this.lightColor.resolveColor,
            visibility = this.lockscreenVisibility.resolveVisibility,
            showBadge = this.canShowBadge(),
            vibration = if (!this.shouldVibrate()) Vibration.NONE
            else vibrationPattern.resolvePattern,
            audio = sound.reverseSound,
        )
    }

    val Int.resolveImportance
        @RequiresApi(O)
        get() : Importance = run {
            when (this) {
                IMPORTANCE_UNSPECIFIED -> UNSPECIFIED
                IMPORTANCE_NONE -> NONE
                IMPORTANCE_MIN -> MIN
                IMPORTANCE_LOW -> LOW
                IMPORTANCE_DEFAULT -> DEFAULT
                IMPORTANCE_HIGH -> HIGH
                IMPORTANCE_MAX -> MAX
                else -> throw IllegalStateException("")
            }
        }

    val Int.resolveVisibility
        @RequiresApi(O)
        get() : Visibility = run {
            when (this) {
                VISIBILITY_PUBLIC -> PUBLIC
                VISIBILITY_PRIVATE -> PRIVATE
                VISIBILITY_SECRET -> SECRET
                VISIBILITY_NO_OVERRIDE -> NO_OVERRIDE
                else -> {
                    println(this)
                    UNKNOWN
                }
            }
        }

    private val LongArray.resolvePattern
        get() = run {
            if (this.contentEquals(Vibration.Patterns.patternOne)) Vibration.ONE
            else if (this.contentEquals(Vibration.Patterns.patternTwo)) Vibration.TWO
            else Vibration.DEFAULT
        }

    private val Uri.reverseSound get() = run {
        if (equals(Sound.Uris.dogUri)) Sound.DOG
        else if (equals(Sound.Uris.sickUri)) Sound.SICK
        else if (equals(Sound.Uris.retroUri)) Sound.RETRO
        else Sound.DEFAULT
    }

    val NotificationChannelGroup.descriptionCompat: String
        get() = run {
            if (SDK_INT >= P) "${description.take(6)}..." else "Description not allowed"
        }
}
