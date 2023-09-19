package com.chrrissoft.notifications.channels

import android.app.NotificationManager
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.Build.VERSION_CODES.O
import android.provider.Settings.*
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrrissoft.notifications.NotificationsApp
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.*
import com.chrrissoft.notifications.channels.ChannelsState.*
import com.chrrissoft.notifications.channels.ChannelsState.Page.BUILDER
import com.chrrissoft.notifications.channels.ChannelsState.Page.GROUP_BUILDER
import com.chrrissoft.notifications.channels.Util.copy
import com.chrrissoft.notifications.channels.Util.descriptionCompat
import com.chrrissoft.notifications.shared.SnackbarData
import com.chrrissoft.notifications.shared.SnackbarData.MessageType.Error
import com.chrrissoft.notifications.shared.SnackbarData.MessageType.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
@RequiresApi(O)
class ChannelsViewModel @Inject constructor(
    private val manager: NotificationManager,
    private val ctx: NotificationsApp
) : ViewModel() {
    private val _state = MutableStateFlow(ChannelsState())
    val stateFlow = _state.asStateFlow()
    private val state get() = stateFlow.value
    private val _page get() = state.page
    private val _builder get() = state.builder
    private val _listing get() = state.list
    private val _groupBuilder get() = state.groupBuilder
    private val _groupListing get() = state.groupList
    private val _snackbarData get() = state.snackbarData
    private val _error get() = state.error
    private val handler = EventHandler()

    fun handleEvent(event: ChannelsScreenEvent) {
        event.resolve(handler)
    }

    inner class EventHandler {
        val builderHandler = BuilderHandler()
        val listHandler = ListHandler()
        val groupBuilderHandler = GroupBuilderHandler()
        val groupListHandler = GroupListHandler()

        inner class BuilderHandler {
            fun onEvent(event: BuilderEvent.OnChange) {
                updateState(builder = event.data)
            }

            fun onEvent(event: BuilderEvent.OnCreate) {
                if (event.data.name.isEmpty()) {
                    updateState(snackbarData = _snackbarData.copy(type = Error))
                    viewModelScope.launch {
                        _snackbarData.state.showSnackbar(message = "Add Name to builder")
                    }
                    return
                }
                val currentId = "com.chrrissoft.notifications.${_builder.id}"
                _snackbarData.state.currentSnackbarData?.dismiss()
                try {
                    manager.createNotificationChannel(event.data.build())
                    val builder = _builder.copy(id = Random.nextLong())
                    val snackbarData = _snackbarData.copy(type = Success)
                    updateState(builder = builder, snackbarData = snackbarData)
                    viewModelScope.launch {
                        val result = run {
                            _snackbarData.state.showSnackbar(
                                message = "Builder was added! 💚",
                                actionLabel = "View",
                                withDismissAction = true,
                                duration = Short,
                            )
                        }
                        if (result==ActionPerformed) {
                            listHandler.onEvent(ListingEvent.OnOpen(currentId))
                        }
                    }
                } catch (e: Exception) {
                    val snackbarData = _snackbarData.copy(type = Error)
                    val error = run {
                        ErrorState(
                            exception = e.javaClass.simpleName,
                            message = "${e.message}",
                            visible = false
                        )
                    }
                    updateState(snackbarData = snackbarData, error = error)
                    viewModelScope.launch {
                        val result = run {
                            _snackbarData.state.showSnackbar(
                                message = "Error 😫",
                                actionLabel = "View",
                                withDismissAction = true,
                                duration = Short,
                            )
                        }
                        if (result==ActionPerformed) {
                            updateState(error = _error.copy(visible = true))
                        }
                    }
                }
            }

            fun onRequestGroups() {
                if (Build.VERSION.SDK_INT < O) return
                val groups = manager.notificationChannelGroups.plus(null)
                updateState(builder = _builder.copy(groups = groups))
            }
        }

        inner class ListHandler {
            fun onEvent(event: ListingEvent.OnCopy) {
                updateState(page = BUILDER, builder = event.data.copy())
                val groupName = run {
                    manager
                        .notificationChannelGroups
                        .find { _builder.group?.first==it.id }
                        ?.name ?: "Null"
                }
                val group = _builder.group?.copy(second = "$groupName")
                updateState(builder = _builder.copy(group = group))
            }

            fun onEvent(event: ListingEvent.OnOpen) {
                val intent = Intent(ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                    putExtra(EXTRA_APP_PACKAGE, ctx.packageName)
                    putExtra(EXTRA_CHANNEL_ID, event.data)
                    addFlags(FLAG_ACTIVITY_NEW_TASK)
                }
                ctx.startActivity(intent)
            }

            fun onEvent(event: ListingEvent.OnDelete) {
                manager.deleteNotificationChannel(event.data.id)
                onRequest()
            }

            fun onRequest() {
                val channels = manager.notificationChannels
                updateState(listing = _listing.copy(channels = channels))
            }

            fun onEvent(event: ListingEvent.OnBindGroup) {
                event.data.group = event.id
                manager.createNotificationChannel(event.data)
            }

            fun onRequestGroups() {
                val groups = manager.notificationChannelGroups.plus(null)
                updateState(listing = _listing.copy(groups = groups))
            }
        }

        inner class GroupBuilderHandler {
            fun onEvent(event: GroupBuilderEvent.OnChangeState) {
                updateState(groupBuilder = event.data)
            }

            fun onEvent(event: GroupBuilderEvent.OnCreate) {
                if (event.data.name.isEmpty()) {
                    updateState(snackbarData = _snackbarData.copy(type = Error))
                    viewModelScope.launch {
                        _snackbarData.state.showSnackbar(message = "Add name to channel group")
                    }
                    return
                }
                manager.createNotificationChannelGroup(event.data.build())
                val list = manager.notificationChannelGroups
                val newList = _groupListing.copy(groups = list)
                updateState(groupListing = newList, snackbarData = _snackbarData.copy(type = Success))
                viewModelScope.launch {
                    _snackbarData.state.showSnackbar("Channel group was created! 💚")
                }
            }
        }

        inner class GroupListHandler {
            fun onEvent(event: GroupListEvent.OnCopy) {
                val newBuilder = run {
                    GroupBuilderState(
                        name = "${event.data.name}",
                        description = event.data.descriptionCompat
                    )
                }
                updateState(page = GROUP_BUILDER, groupBuilder = newBuilder)
            }

            fun onEvent(event: GroupListEvent.OnDelete) {
                manager.deleteNotificationChannelGroup(event.data.id)
                onRequest()
            }

            fun onRequest() {
                val data = manager.notificationChannelGroups
                updateState(groupListing = _groupListing.copy(groups = data))
            }
        }

        fun onEvent(event: OnChangePage) {
            updateState(page = event.data)
        }

        fun onEvent(event: OnChangeState) {
            _state.update { event.data }
        }
    }

    private fun updateState(
        page: Page = _page,
        builder: BuilderState = _builder,
        listing: ListingState = _listing,
        groupBuilder: GroupBuilderState = _groupBuilder,
        groupListing: GroupListState = _groupListing,
        snackbarData: SnackbarData = _snackbarData,
        error: ErrorState = _error
    ) {
        _state.update {
            it.copy(
                page = page,
                builder = builder,
                list = listing,
                groupList = groupListing,
                groupBuilder = groupBuilder,
                snackbarData = snackbarData,
                error = error
            )
        }
    }
}
