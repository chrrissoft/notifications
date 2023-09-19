package com.chrrissoft.notifications.actions

import android.app.Notification
import android.content.Context
import android.graphics.drawable.Icon
import com.chrrissoft.notifications.shared.PendingIntentBuilder
import com.chrrissoft.notifications.shared.PendingIntentBuilder.Companion.nonNullList


data class ActionsState(
    val builder: ActionBuilder = ActionBuilder(),
    val builders: List<ActionBuilder> = emptyList(),
) {
    data class ActionBuilder(
        val icon: Int = actionIcons.first(),
        val title: String = "",
        val pendingIntentBuilder: PendingIntentBuilder = nonNullList.first(),
    ) {
        fun build(ctx: Context): Notification.Action {
            val `package` = "com.chrrissoft.notifications"
            val icon = Icon.createWithResource(`package`, icon)
            return Notification.Action.Builder(icon, title, pendingIntentBuilder.build(ctx))
                .build()
        }
    }
}
