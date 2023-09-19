package com.chrrissoft.notifications.expandable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent.OnCreate
import com.chrrissoft.notifications.expandable.ExpandableEvent.OnChangePage
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.Builder.*
import com.chrrissoft.notifications.expandable.ExpandableState.Page.BUILDER
import com.chrrissoft.notifications.expandable.ExpandableState.Page.LISTING
import com.chrrissoft.notifications.notifications.NotificationBuilderPage
import com.chrrissoft.notifications.notifications.NotificationsEvent
import com.chrrissoft.notifications.notifications.NotificationsState
import com.chrrissoft.notifications.notifications.NotificationsViewModel
import com.chrrissoft.notifications.notifications.NotificationsViewModel.EventHandler
import com.chrrissoft.notifications.shared.NotificationsSnackbar
import com.chrrissoft.notifications.ui.components.TopBarTitle
import com.chrrissoft.notifications.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.notifications.ui.theme.navigationBarItemColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableScreen(
    state: ExpandableState,
    onEvent: (ExpandableEvent) -> Unit,
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
                title = { TopBarTitle(title = "Expandable Notifications") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (state.builder.builder) {
                        Media -> onEvent(OnCreate(state.builder.mediaStyle.copy()))
                        Call -> onEvent(OnCreate(state.builder.callStyle.copy()))
                        BigPicture -> onEvent(OnCreate(state.builder.bigPictureStyle.copy()))
                        BigText -> onEvent(OnCreate(state.builder.bigTextStyle.copy()))
                        Inbox -> onEvent(OnCreate(state.builder.inboxStyle.copy()))
                        Messaging -> onEvent(OnCreate(state.builder.messagingStyle.copy()))
                        DecoratedCustomView -> onEvent(OnCreate(state.builder.decoratedCustomViewStyle.copy()))
                    }
                }
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        },
        containerColor = colorScheme.onPrimary,
        snackbarHost = {
            NotificationsSnackbar(data = state.snackbarData)
        },
        bottomBar = {
            NavigationBar(containerColor = colorScheme.primaryContainer) {
                ExpandableState.Page.pages.forEach {
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
    state: ExpandableState,
    onEvent: (ExpandableEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state.page) {
        BUILDER -> {
            ExpandableBuilderPage(
                state = state.builder,
                onEvent = onEvent,
                modifier = modifier
            )
        }
        LISTING -> {
            ExpandableListingPage(
                state = state.listing,
                onEvent = onEvent,
                modifier = modifier,
            )
        }
    }
}
