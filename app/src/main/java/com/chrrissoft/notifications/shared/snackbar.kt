package com.chrrissoft.notifications.shared

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun NotificationsSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier
) {
    SnackbarHost(hostState = data.state, modifier = modifier) {
        Snackbar(
            snackbarData = it,
            containerColor = data.type.containerColor,
            contentColor = data.type.contentColor,
            actionColor = data.type.actionColor,
            actionContentColor = data.type.actionContentColor,
            dismissActionContentColor = data.type.dismissActionContentColor,
        )
    }
}
