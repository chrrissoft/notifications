package com.chrrissoft.notifications.notifications

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrrissoft.notifications.NotificationsApp
import com.chrrissoft.notifications.R
import com.chrrissoft.notifications.Util.showSnackbarOwn
import com.chrrissoft.notifications.notifications.NotificationsEvent.*
import com.chrrissoft.notifications.notifications.NotificationsEvent.BuilderEvent.*
import com.chrrissoft.notifications.notifications.NotificationsEvent.GroupBuilderEvent.OnChangeCurrent
import com.chrrissoft.notifications.notifications.NotificationsEvent.ListingEvent.*
import com.chrrissoft.notifications.notifications.NotificationsState.BuilderState
import com.chrrissoft.notifications.notifications.NotificationsState.BuilderState.Mode.CREATING
import com.chrrissoft.notifications.notifications.NotificationsState.BuilderState.Mode.EDITING
import com.chrrissoft.notifications.notifications.NotificationsState.Page.BUILDER
import com.chrrissoft.notifications.shared.SnackbarData
import com.chrrissoft.notifications.shared.SnackbarData.MessageType.Error
import com.chrrissoft.notifications.shared.SnackbarData.MessageType.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random.Default.nextInt
import com.chrrissoft.notifications.notifications.NotificationsEvent.BuilderEvent.OnCreate as OnCreateBuilder
import com.chrrissoft.notifications.notifications.NotificationsEvent.BuilderEvent.OnEdit as OnEditBuilder
import com.chrrissoft.notifications.notifications.NotificationsEvent.GroupBuilderEvent.OnCreate as OnCreateGroup
import com.chrrissoft.notifications.notifications.NotificationsEvent.GroupBuilderEvent.OnDelete as OnDeleteGroup

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val app: NotificationsApp,
    private val manager: NotificationManagerCompat,
) : ViewModel() {
    private val _state = MutableStateFlow(NotificationsState())
    val stateFlow = _state.asStateFlow()
    private val state get() = stateFlow.value
    private val _page get() = state.page
    private val _builder get() = state.builder
    private val _listing get() = state.listing
    private val _groupBuilder get() = state.groupBuilder
    private val _snackbarData get() = state.snackbarData
    private val handler = EventHandler()

    fun handleEvent(event: NotificationsEvent) {
        event.resolve(handler)
    }

    inner class EventHandler() {
        val builder = BuilderEventHandler()

        val listing = ListingEventHandler()

        val groupBuilder = GroupBuilderEventHandler()

        inner class BuilderEventHandler {
            fun onEvent(event: OnChangeBuilder) {
                updateState(builder = event.data)
            }

            fun onEvent(event: OnGroupRequest) {
                val groups = _groupBuilder.set.plus(null)
                if (groups===event.data.groups) return
                updateState(builder = _builder.copy(groups = groups))
            }

            fun onEvent(event: OnSelfListRequest) {
                val selfList = _listing.list.plus((null))
                if (selfList===event.data.selfList) return
                updateState(builder = _builder.copy(selfList = selfList))
            }

            fun onEvent(event: OnChannelsListRequest) {
                if (SDK_INT < O) return
                val channelsList = manager.notificationChannels.map { Pair(it.id, "${it.name}") }
                if (channelsList==event.data.channelsList) return
                updateState(builder = _builder.copy(channelsList = channelsList))
            }

            fun onEvent(event: OnCreateBuilder) {
                if (!validateBuilder(event.data)) return
                debug("creating builder")
                val cachedBuilder = event.data
                val plus = _listing.list.plus(event.data)
                val listing = _listing.copy(list = plus)
                updateState(
                    listing = listing,
                    builder = _builder.copy(id = nextInt()),
                    snackbarData = _snackbarData.copy(type = Success)
                )
                editedOrCreatedSnackbar(edit = false, builder = cachedBuilder)
            }

            fun onEvent(event: OnEditBuilder) {
                if (!validateBuilder(event.data)) return
                debug("editing builder")
                val cachedBuilder = event.data
                val edited = _listing.list.map {
                    if (it.id==event.data.id) event.data else it
                }
                val listing = _listing.copy(list = edited)
                updateState(
                    listing = listing,
                    builder = _builder.copy(id = nextInt(), mode = CREATING),
                    snackbarData = _snackbarData.copy(type = Success)
                )
                editedOrCreatedSnackbar(edit = true, builder = cachedBuilder)
            }

            private fun validateBuilder(data: BuilderState): Boolean {
                return if (data.contentTitle.isEmpty()) {
                    updateState(snackbarData = _snackbarData.copy(type = Error))
                    viewModelScope.launch {
                        _snackbarData.state.showSnackbar("Add notification title")
                    }
                    false
                } else if (data.channel == null && SDK_INT >= O) {
                    viewModelScope.launch {
                        _snackbarData.state.showSnackbar("Add Channel")
                    }
                    false
                } else true
            }

            private fun editedOrCreatedSnackbar(
                edit: Boolean,
                builder: BuilderState,
            ) {
                _snackbarData.state.showSnackbarOwn(
                    scope = viewModelScope,
                    message = run {
                        if (edit) app.getString(R.string.notifications_edited)
                        else app.getString(R.string.notifications_created)
                    },
                    actionLabel = "Launch",
                    onPerformed = {
                        manager.notify(builder.id, builder.build(app))
                    }
                )
            }
        }

        inner class ListingEventHandler {
            fun onEvent(event: OnCopy) {
                updateState(builder = event.data, page = BUILDER)
            }

            fun onEvent(event: OnDelete) {
                debug("deleting builder")
                val minus = _listing.list.filterNot { it===event.data }
                val listing = _listing.copy(list = minus)
                updateState(listing = listing)
            }

            fun onEvent(event: OnLaunch) {
                val id = if (event.semeId) event.data.id else nextInt()
                manager.notify(id, event.data.build(app))
            }

            fun onEvent(event: OnDeleteFromDrawer) {
                manager.cancel(event.data)
            }

            fun onEvent(event: OnEditRequest) {
                val builder = event.data.copy(mode = EDITING)
                updateState(page = BUILDER, builder = builder)
            }
        }

        inner class GroupBuilderEventHandler {
            private val keys = mutableMapOf<String, Int>()

            fun onEvent(event: OnCreateGroup) {
                debug("creating group")
                if (event.data.isEmpty()) return
                if (keys.contains(event.data)) {
                    val count = keys[event.data]!!.plus(1)
                    keys[event.data] = count
                    val newSet = _groupBuilder.set.plus(event.data.plus(" $count"))
                    val newBuilder = _groupBuilder.copy(set = newSet)
                    updateState(groupBuilder = newBuilder)
                } else {
                    keys[event.data] = 1
                    val newSet = _groupBuilder.set.plus(event.data)
                    val newBuilder = _groupBuilder.copy(set = newSet)
                    updateState(groupBuilder = newBuilder)
                }
                debug("created group")
            }

            fun onEvent(event: OnDeleteGroup) {
                val new = _groupBuilder.set.minus(event.data)
                val newBuilder = _groupBuilder.copy(set = new)
                updateState(groupBuilder = newBuilder)
            }

            fun onEvent(event: OnChangeCurrent) {
                val newBuilder = _groupBuilder.copy(current = event.data)
                updateState(groupBuilder = newBuilder)
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
        page: NotificationsState.Page = _page,
        builder: BuilderState = _builder,
        listing: NotificationsState.ListingState = _listing,
        groupBuilder: NotificationsState.GroupBuilderState = _groupBuilder,
        snackbarData: SnackbarData = _snackbarData
    ) {
        _state.update {
            it.copy(
                page = page,
                builder = builder,
                listing = listing,
                groupBuilder = groupBuilder,
                snackbarData = snackbarData,
            )
        }
    }

    private companion object {
        private const val TAG = "NotificationsViewModel"
        private fun debug(message: Any?) {
            Log.d(TAG, message.toString())
        }
    }
}
