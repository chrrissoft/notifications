package com.chrrissoft.notifications.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.notifications.ui.theme.NotificationsTheme

@Composable
fun App(app: @Composable () -> Unit) {
    NotificationsTheme {
        Surface(
            color = colorScheme.onPrimary,
            modifier = Modifier.fillMaxSize(),
        ) { app() }
    }
}
