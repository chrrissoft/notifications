package com.chrrissoft.notifications.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.notifications.Util.ui
import com.chrrissoft.notifications.shared.Vibration.Patterns.patternOne
import com.chrrissoft.notifications.shared.Vibration.Patterns.patternTwo

enum class Vibration(val icon: ImageVector, val original: LongArray) {
    NONE(icon = Icons.Rounded.Favorite, LongArray(0)),
    DEFAULT(icon = Icons.Rounded.Favorite, LongArray(0)),
    ONE(icon = Icons.Rounded.Favorite, patternOne),
    TWO(icon = Icons.Rounded.Favorite, patternTwo),
    ;

    val label: String = this.name.ui
    companion object {
        val list = buildList {
            add(NONE)
            add(DEFAULT)
            add(ONE)
            add(TWO)
        }
    }

    object Patterns {
        val patternOne = longArrayOf(0, 25, 25, 25, 25, 25, 25, 25, 100, 250)
        val patternTwo = longArrayOf(0, 250, 100, 25, 25, 25, 25, 25, 25, 25)
    }
}
