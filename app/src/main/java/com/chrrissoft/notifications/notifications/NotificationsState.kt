package com.chrrissoft.notifications.notifications

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.*
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.app.NotificationCompat
import com.chrrissoft.notifications.MainActivity
import com.chrrissoft.notifications.MainReceiver
import com.chrrissoft.notifications.MainService
import com.chrrissoft.notifications.Util.ui
import com.chrrissoft.notifications.shared.*
import android.app.Notification.*
import com.chrrissoft.notifications.R as Resources
import kotlin.random.Random


data class NotificationsState(
    val page: Page = Page.BUILDER,
    val builder: BuilderState = BuilderState(),
    val listing: ListingState = ListingState(),
    val groupBuilder: GroupBuilderState = GroupBuilderState(),
    val snackbarData: SnackbarData = SnackbarData()
) {
    enum class Page(val icon: ImageVector) {
        BUILDER(Icons.Rounded.Favorite),
        LISTING(Icons.Rounded.Favorite),
        GROUP_BUILDER(Icons.Rounded.Favorite);

        val label: String = this.name.ui

        companion object {
            val pages = buildList {
                add(BUILDER)
                add(LISTING)
                add(GROUP_BUILDER)
            }
        }
    }

    data class BuilderState(
        val onlyAlertOnce: Boolean = true,
        val group: String? = null,
        val channel: Pair<String, String>? = null,
        val publicVersion: BuilderState? = null,
        val number: Int = 0,
        val groupAlertBehavior: GroupAlertBehavior = GroupAlertBehavior.NONE,
        val whenTime: Long? = null,
        val timeoutAfter: Long? = null,
        val color: Color = Color.NONE,
        val lights: Lights = Lights(),
        val subText: String = "",
        val contentInfo: String = "",
        val contentText: String = "",
        val contentTitle: String = "",
        val badge: BadgeIconType = BadgeIconType.NONE,
        val category: Category = Category.NONE,
        val contentIntent: PendingIntentBuilder? = null,
        val deleteIntent: PendingIntentBuilder? = null,
        val groupSummary: Boolean = false,
        val autoActions: Boolean = true,
        val autoCancel: Boolean = false,
        val showWhen: Boolean = true,
        val silent: Boolean = false,
        val ongoing: Boolean = false,
        val sound: Sound = Sound.DEFAULT,
        val vibration: Vibration = Vibration.NONE,
        val priority: Priority = Priority.HIGH,
        val visibility: Visibility = Visibility.PUBLIC,
        // ----------------------------------------------------------------
        val mode: Mode = Mode.CREATING,
        val groups: Set<String?> = emptySet(),
        val selfList: List<BuilderState?> = emptyList(),
        val channelsList: List<Pair<String, String>> = emptyList(),
        val id: Int = Random.nextInt(),
    ) {
        val numberUi = if (number <= 0) "null" else "$number"

        data class Lights(
            val color: Color = Color.BLUE,
            val onMs: Int = 200,
            val offMs: Int = 200,
        )

        enum class BadgeIconType(val icon: ImageVector, val original: Int) {
            NONE(Icons.Rounded.Favorite, original = if (SDK_INT >= O) BADGE_ICON_NONE else 0),
            SMALL(Icons.Rounded.Favorite, original = if (SDK_INT >= O) BADGE_ICON_SMALL else 0),
            LARGE(Icons.Rounded.Favorite, original = if (SDK_INT >= O) BADGE_ICON_LARGE else 0);

            val label: String = this.name.ui

            companion object {
                val list = buildList {
                    add(NONE)
                    add(SMALL)
                    add(LARGE)
                }
            }
        }

        enum class Category(val icon: ImageVector, val original: String) {
            NONE(Icons.Rounded.Favorite, ""),
            CALL(Icons.Rounded.Favorite, CATEGORY_CALL),

            @RequiresApi(P)
            NAVIGATION(Icons.Rounded.Favorite, CATEGORY_NAVIGATION),
            MESSAGE(Icons.Rounded.Favorite, CATEGORY_MESSAGE),
            EMAIL(Icons.Rounded.Favorite, CATEGORY_EMAIL),
            EVENT(Icons.Rounded.Favorite, CATEGORY_EVENT),
            PROMO(Icons.Rounded.Favorite, CATEGORY_PROMO),
            ALARM(Icons.Rounded.Favorite, CATEGORY_ALARM),
            PROGRESS(Icons.Rounded.Favorite, CATEGORY_PROGRESS),
            SOCIAL(Icons.Rounded.Favorite, CATEGORY_SOCIAL),
            ERROR(Icons.Rounded.Favorite, CATEGORY_ERROR),
            TRANSPORT(Icons.Rounded.Favorite, CATEGORY_TRANSPORT),
            SYSTEM(Icons.Rounded.Favorite, CATEGORY_SYSTEM),
            SERVICE(Icons.Rounded.Favorite, CATEGORY_SERVICE),
            REMINDER(Icons.Rounded.Favorite, CATEGORY_REMINDER),
            RECOMMENDATION(Icons.Rounded.Favorite, CATEGORY_RECOMMENDATION),
            STATUS(Icons.Rounded.Favorite, CATEGORY_STATUS),

            @RequiresApi(S)
            WORKOUT(Icons.Rounded.Favorite, CATEGORY_WORKOUT),

            @RequiresApi(S)
            LOCATION_SHARING(Icons.Rounded.Favorite, CATEGORY_LOCATION_SHARING),

            @RequiresApi(S)
            STOPWATCH(Icons.Rounded.Favorite, CATEGORY_STOPWATCH),

            @RequiresApi(S)
            MISSED_CALL(Icons.Rounded.Favorite, CATEGORY_MISSED_CALL);

            val label: String = this.name.ui

            companion object {
                val list = buildList {
                    add(NONE)
                    add(CALL)
                    if (SDK_INT >= P) add(NAVIGATION)
                    add(MESSAGE)
                    add(EMAIL)
                    add(EVENT)
                    add(PROMO)
                    add(ALARM)
                    add(PROGRESS)
                    add(SOCIAL)
                    add(ERROR)
                    add(TRANSPORT)
                    add(SYSTEM)
                    add(SERVICE)
                    add(REMINDER)
                    add(RECOMMENDATION)
                    add(STATUS)
                    if (SDK_INT >= S) {
                        add(WORKOUT)
                        add(LOCATION_SHARING)
                        add(STOPWATCH)
                        add(MISSED_CALL)
                    }
                }
            }
        }

        enum class GroupAlertBehavior(val icon: ImageVector, val original: Int) {
            NONE(Icons.Rounded.Favorite, 0),

            @RequiresApi(O)
            ALL(Icons.Rounded.Favorite, GROUP_ALERT_ALL),

            @RequiresApi(O)
            SUMMARY(Icons.Rounded.Favorite, GROUP_ALERT_SUMMARY),

            @RequiresApi(O)
            CHILDREN(Icons.Rounded.Favorite, GROUP_ALERT_CHILDREN);

            val label: String = this.name.ui

            companion object {
                val list = buildList {
                    add(NONE)
                    if (SDK_INT >= O) {
                        add(ALL)
                        add(SUMMARY)
                        add(CHILDREN)
                    }
                }
            }
        }

        enum class Priority(val icon: ImageVector, val original: Int) {
            DEFAULT(Icons.Rounded.Favorite, PRIORITY_DEFAULT),
            LOW(Icons.Rounded.Favorite, PRIORITY_LOW),
            MIN(Icons.Rounded.Favorite, PRIORITY_MIN),
            HIGH(Icons.Rounded.Favorite, PRIORITY_HIGH),
            MAX(Icons.Rounded.Favorite, PRIORITY_MAX);

            val label: String = this.name.ui

            companion object {
                val list = buildList {
                    add(DEFAULT)
                    add(LOW)
                    add(MIN)
                    add(HIGH)
                    add(MAX)
                }
            }
        }

        enum class Mode {
            CREATING, EDITING,
        }

        fun build(ctx: Context): Notification {
            return NotificationCompat
                .Builder(ctx, "${channel?.first}")
                .apply {
                    setOnlyAlertOnce(onlyAlertOnce)
                    setGroup(group)
                    setPublicVersion(publicVersion?.build(ctx))
                    setNumber(number)
                    setGroupAlertBehavior(groupAlertBehavior.original)
                    whenTime?.let { setWhen(it) }
                    timeoutAfter?.let { setTimeoutAfter(it) }
                    color = this@BuilderState.color.original
                    setLights(lights.color.original, lights.onMs, lights.offMs)
                    setSubText(subText)
                    setContentInfo(contentInfo)
                    setContentText(contentText)
                    setContentTitle(contentTitle)
                    if (SDK_INT>=O) setBadgeIconType(badge.original)
                    setCategory(category.original)
                    setContentIntent(contentIntent?.build(ctx))
                    setDeleteIntent(deleteIntent?.build(ctx))
                    setGroupSummary(groupSummary)
                    setAllowSystemGeneratedContextualActions(autoActions)
                    setAutoCancel(autoCancel)
                    setShowWhen(showWhen)
                    setSilent(silent)
                    setOngoing(ongoing)
                    setSound(sound.notificationUri)
                    setVibrate(vibration.original)
                    priority = this@BuilderState.priority.original
                    setVisibility(visibility.original)

                    setSmallIcon(Resources.drawable.ic_launcher_foreground)
                }
                .build()
        }
    }

    data class ListingState(
        val list: List<BuilderState> = emptyList(),
    )

    data class GroupBuilderState(
        val current: String = "",
        val set: Set<String> = emptySet(),
    )
}

//          setSound        //
//          setLights       //
//          setVibrate      //
//          setPriority     //
//          setVisibility   //
// setLocusId
