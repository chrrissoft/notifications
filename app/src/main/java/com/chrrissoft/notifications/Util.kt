package com.chrrissoft.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.chrrissoft.notifications.shared.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color as ComposeColor

object Util {
    @SuppressLint("ComposableNaming")
    @Composable
    fun setBarsColors(
        status: ComposeColor = MaterialTheme.colorScheme.primaryContainer,
        bottom: ComposeColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()

        DisposableEffect(systemUiController, useDarkIcons) {
            systemUiController.setStatusBarColor(status, useDarkIcons)
            systemUiController.setNavigationBarColor(bottom)
            onDispose {}
        }
    }

    fun DrawerState.open(scope: CoroutineScope) {
        scope.launch { open() }
    }

    fun DrawerState.close(scope: CoroutineScope) {
        scope.launch { close() }
    }

    val String.ui get() = replace("_", " ").lowercase()

    val String.toUri: Uri get() = Uri.parse("android.resource://com.chrrissoft.notifications/raw/$this")
    val Int.toUri: Uri get() = Uri.parse("android.resource://com.chrrissoft.notifications/$this")

    fun MediaPlayer.play(ctx: Context, uri: Uri) {
        reset()
        setDataSource(ctx, uri)
        prepare()
        start()
    }

    val Int.resolveColor
        get() = run {
            if (this==-16711936) Color.GREEN
            else if (this==0) Color.NONE
            else if (this==-65536) Color.RED
            else if (this==-16776961) Color.BLUE
            else Color.NONE
        }

    fun SnackbarHostState.showSnackbarOwn(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = actionLabel != null,
        duration: SnackbarDuration = if (actionLabel==null) Short else Indefinite,
        scope: CoroutineScope,
        onPerformed: (() -> Unit)? = null,
        dismissCurrent: Boolean = true,
    ) {
        if (dismissCurrent) this.currentSnackbarData?.dismiss()
        scope.launch {
            showSnackbar(message, actionLabel, withDismissAction, duration).let {
                if (it==ActionPerformed) onPerformed?.let { it1 -> it1() }
            }
        }
    }
}
