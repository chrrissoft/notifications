package com.chrrissoft.notifications.navigation

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chrrissoft.notifications.Util.close
import com.chrrissoft.notifications.Util.open
import com.chrrissoft.notifications.actions.ActionsScreen
import com.chrrissoft.notifications.actions.ActionsViewModel
import com.chrrissoft.notifications.channels.ChannelsScreen
import com.chrrissoft.notifications.channels.ChannelsViewModel
import com.chrrissoft.notifications.common.CommonScreen
import com.chrrissoft.notifications.common.CommonViewModel
import com.chrrissoft.notifications.conversations.ConversationsScreen
import com.chrrissoft.notifications.conversations.ConversationsViewModel
import com.chrrissoft.notifications.custom.CustomScreen
import com.chrrissoft.notifications.custom.CustomViewModel
import com.chrrissoft.notifications.expandable.ExpandableScreen
import com.chrrissoft.notifications.expandable.ExpandableViewModel
import com.chrrissoft.notifications.navigation.Screens.*
import com.chrrissoft.notifications.navigation.Screens.Companion.screens
import com.chrrissoft.notifications.notifications.NotificationsScreen
import com.chrrissoft.notifications.notifications.NotificationsViewModel
import com.chrrissoft.notifications.ui.theme.navigationDrawerItemColors

@Composable
fun Graph() {
    val controller = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = Closed)
    val scope = rememberCoroutineScope()
    val backStack by controller.currentBackStackEntryAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = colorScheme.primaryContainer
            ) {
                DrawerHeader()
                LazyColumn {
                    items(screens) {
                        NavigationDrawerItem(
                            label = { Text(text = it.label) },
                            selected = backStack?.destination?.route==it.route,
                            onClick = {
                                drawerState.close(scope)
                                if (!controller.popBackStack(it.route, false)) {
                                    controller.navigate(it.route) {
                                        launchSingleTop = true
                                    }
                                }
                            },
                            icon = {
                                Icon(imageVector = it.icon, contentDescription = null)
                            },
                            colors = navigationDrawerItemColors,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        },
    ) {
        NavHost(controller, EXPANDABLE.route) {
            composable(CHANNELS.route) {
                if (SDK_INT < O) return@composable
                val viewModel = hiltViewModel<ChannelsViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                ChannelsScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }

            composable(NOTIFICATIONS.route) {
                val viewModel = hiltViewModel<NotificationsViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                NotificationsScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }

            composable(CONVERSATION.route) {
                val viewModel = hiltViewModel<ConversationsViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                ConversationsScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }

            composable(COMMON.route) {
                val viewModel = hiltViewModel<CommonViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                CommonScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }

            composable(CUSTOM.route) {
                val viewModel = hiltViewModel<CustomViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                CustomScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }

            composable(EXPANDABLE.route) {
                val viewModel = hiltViewModel<ExpandableViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                ExpandableScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }

            composable(ACTIONS.route) {
                val viewModel = hiltViewModel<ActionsViewModel>()
                val state by viewModel.stateFlow.collectAsState()
                ActionsScreen(
                    state = state,
                    onEvent = { viewModel.handleEvent(it) },
                    onOpenDrawer = { drawerState.open(scope) }
                )
            }
        }
    }
}
