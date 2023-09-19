package com.chrrissoft.notifications.channels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.*
import com.chrrissoft.notifications.channels.ChannelsState.Page.*
import com.chrrissoft.notifications.channels.ChannelsState.Page.Companion.pages
import com.chrrissoft.notifications.shared.NotificationsSnackbar
import com.chrrissoft.notifications.ui.components.TopBarTitle
import com.chrrissoft.notifications.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.notifications.ui.theme.navigationBarItemColors
import com.chrrissoft.notifications.ui.components.AlertDialog
import com.chrrissoft.notifications.ui.theme.alertDialogErrorColors

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsScreen(
    state: ChannelsState,
    onEvent: (ChannelsScreenEvent) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = centerAlignedTopAppBarColors,
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
                    }
                },
                title = { TopBarTitle(title = "Notifications Channels") },
            )
        },
        floatingActionButton = {
            if (state.page==LIST||state.page==GROUP_LIST) return@Scaffold
            FloatingActionButton(
                onClick = {
                    when (state.page) {
                        BUILDER -> {
                            onEvent(BuilderEvent.OnCreate(state.builder))
                        }
                        GROUP_BUILDER -> {
                            onEvent(GroupBuilderEvent.OnCreate(state.groupBuilder))
                        }
                        else -> {}
                    }
                }
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        },
        bottomBar = {
            NavigationBar(containerColor = colorScheme.primaryContainer) {
                pages.forEach {
                    NavigationBarItem(
                        selected = it==state.page,
                        onClick = { onEvent(OnChangePage(it)) },
                        icon = { Icon(imageVector = it.icon, contentDescription = null) },
                        label = { Text(text = it.label) },
                        colors = navigationBarItemColors,
                    )
                }
            }
        },
        containerColor = colorScheme.onPrimary,
        snackbarHost = {
            NotificationsSnackbar(data = state.snackbarData)
        },
        content = {
            if (state.error.visible) {
                AlertDialog(
                    onDismissRequest = {
                        val error = state.error.copy(visible = false)
                        onEvent(OnChangeState(state.copy(error = error)))
                    },
                    title = state.error.exception,
                    text = { Text(text = state.error.message) },
                    onConfirm = {
                        val error = state.error.copy(visible = false)
                        onEvent(OnChangeState(state.copy(error = error)))
                    },
                    colors = alertDialogErrorColors,
                    buttonColors = buttonColors(
                        containerColor = colorScheme.onErrorContainer,
                        contentColor = colorScheme.errorContainer
                    )
                )
            }

            ScreenContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.padding(it)
            )
        },
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ScreenContent(
    state: ChannelsState,
    onEvent: (ChannelsScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.page) {
        BUILDER -> {
            ChannelsBuilderPage(
                state = state.builder,
                onEvent = onEvent,
                modifier = modifier,
            )
        }
        LIST -> {
            ChannelsListPage(
                state = state.list,
                onEvent = onEvent,
                modifier = modifier,
            )
        }
        GROUP_BUILDER -> {
            ChannelsGroupBuilderPage(
                state = state.groupBuilder,
                onEvent = onEvent,
                modifier = modifier,
            )

        }
        GROUP_LIST -> {
            ChannelsGroupListPage(
                state = state.groupList,
                onEvent = onEvent,
                modifier = modifier,
            )
        }
    }
}
