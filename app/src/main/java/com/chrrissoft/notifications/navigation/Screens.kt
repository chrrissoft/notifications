package com.chrrissoft.notifications.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.capitalize
import com.chrrissoft.notifications.Util.ui
import java.util.*

enum class Screens(val route: String, val icon: ImageVector) {
    CHANNELS("CHANNELS", Icons.Rounded.Favorite),
    NOTIFICATIONS("NOTIFICATIONS", Icons.Rounded.Favorite),
    CONVERSATION("CONVERSATION", Icons.Rounded.Favorite),
    COMMON("COMMON", Icons.Rounded.Favorite),
    CUSTOM("CUSTOM", Icons.Rounded.Favorite),
    EXPANDABLE("EXPANDABLE", Icons.Rounded.Favorite),
    ACTIONS("ACTIONS", Icons.Rounded.Favorite),
    ;

    val label = this.name.ui
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

    companion object {
        val screens = buildList {
            add(CHANNELS)
            add(NOTIFICATIONS)
            add(CONVERSATION)
            add(COMMON)
            add(CUSTOM)
            add(EXPANDABLE)
            add(ACTIONS)
        }
    }
}
