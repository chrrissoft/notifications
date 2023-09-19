package com.chrrissoft.notifications.channels

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager.*
import android.media.AudioAttributes.*
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.P
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.notifications.Util.ui
import com.chrrissoft.notifications.channels.ChannelsState.BuilderState.Importance.HIGH
import com.chrrissoft.notifications.shared.*
import com.chrrissoft.notifications.shared.Visibility.PUBLIC
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
data class ChannelsState(
    val page: Page = Page.BUILDER,
    val builder: BuilderState = BuilderState(),
    val list: ListingState = ListingState(),
    val groupBuilder: GroupBuilderState = GroupBuilderState(),
    val groupList: GroupListState = GroupListState(),
    val snackbarData: SnackbarData = SnackbarData(),
    val error: ErrorState = ErrorState(),
) {
    enum class Page(val icon: ImageVector) {
        BUILDER(icon = Rounded.Build),
        LIST(icon = Rounded.List),
        GROUP_BUILDER(icon = Rounded.Build),
        GROUP_LIST(icon = Rounded.List),;

        val label: String = this.name.ui
        companion object {
            val pages = buildList {
                add(BUILDER)
                add(LIST)
                add(GROUP_BUILDER)
                add(GROUP_LIST)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    data class BuilderState(
        val id: Long = Random.nextLong(),
        val name: String = "",
        val group: Pair<String, String>? = null,
        val description: String = "",
        val importance: Importance = HIGH,
        val color: Color = Color.RED,
        val visibility: Visibility = PUBLIC,
        val showBadge: Boolean = true,
        val vibration: Vibration = Vibration.NONE,
        val audio: Sound = Sound.DEFAULT,
        val groups: List<NotificationChannelGroup?> = emptyList(),
    ) {
        @RequiresApi(Build.VERSION_CODES.O)
        enum class Importance(val icon: ImageVector, val original: Int) {
            UNSPECIFIED(Rounded.Favorite, IMPORTANCE_UNSPECIFIED),
            NONE(Rounded.Favorite, IMPORTANCE_NONE),
            MIN(Rounded.Favorite, IMPORTANCE_MIN),
            LOW(Rounded.Favorite, IMPORTANCE_LOW),
            DEFAULT(Rounded.Favorite, IMPORTANCE_DEFAULT),
            HIGH(Rounded.Favorite, IMPORTANCE_HIGH),
            MAX(Rounded.Favorite, IMPORTANCE_MAX);

            val label: String = this.name.ui
            companion object {
                val list = buildList {
                    add(UNSPECIFIED)
                    add(NONE)
                    add(MIN)
                    add(LOW)
                    add(DEFAULT)
                    add(HIGH)
                    add(MAX)
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun build(): NotificationChannel {
            val id = "com.chrrissoft.notifications.$id"
            return NotificationChannel(id, name, importance.original).apply {
                if (this@BuilderState.group!=null) {
                    group = this@BuilderState.group.first
                }
                lockscreenVisibility = visibility.original
                enableVibration(vibration!=Vibration.NONE)
                if (vibration!=Vibration.DEFAULT) vibrationPattern = vibration.original
                if (audio!=Sound.DEFAULT) setSound(audio.notificationUri, audio.attributes)
                if (color==Color.NONE) enableLights(false)
                else lightColor = color.original
                this.description = this@BuilderState.description
                setShowBadge(showBadge)
            }
        }
    }

    data class ListingState(
        val channels: List<NotificationChannel> = emptyList(),
        val groups: List<NotificationChannelGroup?> = emptyList(),
    )

    @RequiresApi(Build.VERSION_CODES.O)
    data class GroupBuilderState(val name: String = "", val description: String = "") {
        fun build(): NotificationChannelGroup {
            val id = ("com.chrrissoft.notifications.").plus("${Random.nextInt()}")
            return NotificationChannelGroup(id, name).apply {
                if (SDK_INT >= P) {
                    this.description = this@GroupBuilderState.description
                }
            }
        }
    }

    data class GroupListState(val groups: List<NotificationChannelGroup> = emptyList())

    data class ErrorState(
        val exception: String = "",
        val message: String = "",
        val visible: Boolean = false
    )
}
