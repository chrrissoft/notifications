package com.chrrissoft.notifications.notifications

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.notifications.channels.*
import com.chrrissoft.notifications.notifications.NotificationsEvent.BuilderEvent.OnEdit
import com.chrrissoft.notifications.notifications.NotificationsEvent.BuilderEvent.OnSelfListRequest
import com.chrrissoft.notifications.notifications.NotificationsEvent.OnChangePage
import com.chrrissoft.notifications.notifications.NotificationsEvent.OnChangeState
import com.chrrissoft.notifications.notifications.NotificationsState.BuilderState.Mode.CREATING
import com.chrrissoft.notifications.notifications.NotificationsState.BuilderState.Mode.EDITING
import com.chrrissoft.notifications.notifications.NotificationsState.Page.*
import com.chrrissoft.notifications.notifications.NotificationsState.Page.Companion.pages
import com.chrrissoft.notifications.shared.NotificationsSnackbar
import com.chrrissoft.notifications.ui.components.TopBarTitle
import com.chrrissoft.notifications.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.notifications.ui.theme.navigationBarItemColors
import com.chrrissoft.notifications.notifications.NotificationsEvent.BuilderEvent.OnCreate as OnCreateBuilder
import com.chrrissoft.notifications.notifications.NotificationsEvent.GroupBuilderEvent.OnCreate as OnCreateGroupBuilder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    state: NotificationsState,
    onEvent: (NotificationsEvent) -> Unit,
    onOpenDrawer: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = centerAlignedTopAppBarColors,
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(imageVector = Rounded.Menu, contentDescription = null)
                    }
                },
                title = { TopBarTitle(title = "Notification Builder") },
            )
        },
        floatingActionButton = {
            if (state.page==LISTING) return@Scaffold
            FloatingActionButton(
                onClick = {
                    when (state.page) {
                        BUILDER -> {
                            when (state.builder.mode) {
                                CREATING -> {
                                    onEvent(OnCreateBuilder(state.builder))
                                    onEvent(OnSelfListRequest(state.builder))
                                }
                                EDITING -> {
                                    onEvent(OnEdit(state.builder))
                                    onEvent(OnSelfListRequest(state.builder))
                                }
                            }
                        }
                        GROUP_BUILDER -> {
                            debug("taping to create a group")
                            onEvent(OnCreateGroupBuilder(state.groupBuilder.current))
                        }
                        else -> {}
                    }
                }
            ) {
                val icon = if (state.builder.mode==EDITING) Rounded.Edit else Rounded.Add
                Icon(imageVector = icon, contentDescription = null)
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
            ScreenContent(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.padding(it)
            )
        },
    )
}

@Composable
private fun ScreenContent(
    state: NotificationsState,
    onEvent: (NotificationsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.page) {
        BUILDER -> {
            NotificationBuilderPage(
                state = state.builder,
                onEvent = onEvent,
                modifier = modifier,
            )
        }
        LISTING -> {
            NotificationListingPage(
                state = state.listing,
                onEvent = onEvent,
                modifier = modifier,
            )
        }
        GROUP_BUILDER -> {
            NotificationGroupBuilderPage(
                state = state.groupBuilder,
                onEvent = onEvent,
                modifier = modifier,
            )
        }
    }
}

private fun debug(message: Any?) {
    Log.d("NotificationsScreen", message.toString())
}
