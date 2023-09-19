package com.chrrissoft.notifications.shared

import android.app.Notification.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.notifications.Util.ui

enum class Visibility(val icon: ImageVector, val original: Int) {
    UNKNOWN(icon = Icons.Rounded.Favorite, original = 0),
    NO_OVERRIDE(icon = Icons.Rounded.Favorite, original = -1000),
    PUBLIC(icon = Icons.Rounded.Favorite, original = VISIBILITY_PUBLIC),
    SECRET(icon = Icons.Rounded.Favorite, original = VISIBILITY_SECRET),
    PRIVATE(icon = Icons.Rounded.Favorite, original = VISIBILITY_PRIVATE),
    ;

    val label: String = this.name.ui
    companion object {
        val list = buildList {
            add(PUBLIC)
            add(SECRET)
            add(PRIVATE)
            add(UNKNOWN)
            add(NO_OVERRIDE)
        }
    }
}
