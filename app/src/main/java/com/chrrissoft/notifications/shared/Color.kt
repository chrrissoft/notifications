package com.chrrissoft.notifications.shared

import androidx.compose.ui.graphics.toArgb
import com.chrrissoft.notifications.Util.ui
import androidx.compose.ui.graphics.Color.Companion as ComposeColor

enum class Color(val original: Int) {
    NONE(0),
    RED(ComposeColor.Red.toArgb()),
    GREEN(ComposeColor.Green.toArgb()),
    BLUE(ComposeColor.Blue.toArgb());

    val label: String = this.name.ui

    companion object {
        val list = buildList {
            add(NONE)
            add(RED)
            add(GREEN)
            add(BLUE)
        }
    }
}
