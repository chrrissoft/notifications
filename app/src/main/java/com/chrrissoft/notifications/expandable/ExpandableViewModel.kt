package com.chrrissoft.notifications.expandable

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarDuration.Short
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrrissoft.notifications.Util.showSnackbarOwn
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent.*
import com.chrrissoft.notifications.expandable.ExpandableEvent.OnChangePage
import com.chrrissoft.notifications.expandable.ExpandableState.*
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.*
import com.chrrissoft.notifications.shared.SnackbarData
import com.chrrissoft.notifications.shared.SnackbarData.MessageType.Error
import com.chrrissoft.notifications.shared.SnackbarData.MessageType.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ExpandableViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(
        ExpandableState())
    val stateFlow = _state.asStateFlow()
    private val state get() = stateFlow.value
    private val _page get() = state.page
    private val _builder get() = state.builder
    private val _listing get() = state.listing
    private val _snackbarData get() = state.snackbarData

    private val handler = EventHandler()

    fun handleEvent(event: ExpandableEvent) {
        event.resolve(handler)
    }

    inner class EventHandler {
        val builder = BuilderEventHandler()
        val listing = ListingEventHandler()

        inner class BuilderEventHandler {
            fun onEvent(event: OnChangeType) {
                val builder = _builder.copy(builder = event.data)
                updateState(builder = builder)
            }

            fun onEvent(event: OnChangeState) {
                println("before " + event.data)
                println("before ${_builder.messagingStyle}")
                val builder = when (event.data) {
                    is BigPictureStyleState -> {
                        _builder.copy(bigPictureStyle = event.data)
                    }
                    is BigTextStyleState -> {
                        _builder.copy(bigTextStyle = event.data)
                    }
                    is CallStyleState -> {
                        _builder.copy(callStyle = event.data)
                    }
                    is DecoratedCustomViewStyleState -> {
                        _builder.copy(decoratedCustomViewStyle = event.data)
                    }
                    is InboxStyleState -> {
                        _builder.copy(inboxStyle = event.data)
                    }
                    is MessagingStyleState -> {
                        _builder.copy(messagingStyle = event.data)
                    }
                    is MediaStyleState -> {
                        _builder.copy(mediaStyle = event.data)
                    }
                }
                updateState(builder = builder)
                println()
                println("after " + event.data)
                println("after ${_builder.messagingStyle}")
            }

            fun onEvent(event: OnCreate) {
                if (!validate(event.data)) return
                val plus = _listing.listing.plus(event.data)
                updateState(listing = _listing.copy(listing = plus))
            }

            private fun validate(data: AbstractStyleState<*>) : Boolean {
                return when (data) {
                    is BigPictureStyleState -> validate(data)
                    is BigTextStyleState -> validate(data)
                    is CallStyleState -> TODO()
                    is DecoratedCustomViewStyleState -> TODO()
                    is InboxStyleState -> validate(data)
                    is MessagingStyleState -> validate(data)
                    is MediaStyleState -> TODO()
                }
            }

            private fun validate(data: MessagingStyleState): Boolean {
                if (data.personBuilder.selection==null) {
                    showSnackbarOwn(message = "Add a person", type = Error)
                    return false
                }
                if (data.message.messages.isEmpty()) {
                    showSnackbarOwn(message = "Add messages", type = Error)
                    return false
                }
                for (it in data.message.messages) {
                    if (it.personBuilder.name.isEmpty()) {
                        showSnackbarOwn(message = "Add a person to all messages", type = Error)
                        return false
                    }
                }
                for (it in data.historyMessage.messages) {
                    if (it.personBuilder.name.isEmpty()) {
                        showSnackbarOwn(message = "Add a person to all history messages", type = Error)
                        return false
                    }
                }
                builderAddedSnackbar()
                return true
            }

            private fun validate(data: BigTextStyleState): Boolean {
                if (data.title.isEmpty()) {
                    showSnackbarOwn(message = "Add Title", type = Error)
                    return false
                }
                if (data.text.isEmpty()) {
                    showSnackbarOwn(message = "Add text", type = Error)
                    return false
                }
                builderAddedSnackbar()
                return true
            }

            private fun validate(data: BigPictureStyleState): Boolean {
                if (data.title.isEmpty()) {
                    showSnackbarOwn(message = "Add Title", type = Error)
                    return false
                }
                if (data.description.isEmpty()) {
                    showSnackbarOwn(message = "Add description", type = Error)
                    return false
                }
                builderAddedSnackbar()
                return true
            }

            private fun validate(data: InboxStyleState): Boolean {
                if (data.title.isEmpty()) {
                    showSnackbarOwn(message = "Add Title", type = Error)
                    return false
                }
                if (data.lines.isEmpty()) {
                    showSnackbarOwn(message = "Add lines", type = Error)
                    return false
                }
                builderAddedSnackbar()
                return true
            }

            private fun builderAddedSnackbar() {
                showSnackbarOwn(message = "Builder was Added!", type = Success)
            }
        }

        inner class ListingEventHandler {
            fun onEvent(event: ExpandableEvent.ListingEvent.OnCopy) {
                TODO("Not yet implemented")
            }

            fun onEvent(event: ExpandableEvent.ListingEvent.OnDelete) {
                TODO("Not yet implemented")
            }

            fun onEvent(event: ExpandableEvent.ListingEvent.OnDeleteFromDrawer) {
                TODO("Not yet implemented")
            }

            fun onEvent(event: ExpandableEvent.ListingEvent.OnEditRequest) {
                TODO("Not yet implemented")
            }

            fun onEvent(event: ExpandableEvent.ListingEvent.OnLaunch) {
                TODO("Not yet implemented")
            }

            fun onEvent(event: ExpandableEvent.ListingEvent.OnChangeType) {
                updateState(listing = _listing.copy(type = event.data))
            }

        }

        fun onEvent(event: OnChangePage) {
            updateState(page = event.data)
        }
    }

    private fun showSnackbarOwn(
        message: String,
        actionLabel: String? = null,
        type: SnackbarData.MessageType,
        withDismissAction: Boolean = actionLabel != null,
        duration: SnackbarDuration = if (actionLabel==null) Short else Indefinite,
        onPerformed: (() -> Unit)? = null,
        dismissCurrent: Boolean = true,
    ) {
        _snackbarData.state.showSnackbarOwn(
            message = message,
            scope = viewModelScope,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
            onPerformed = onPerformed,
            dismissCurrent = dismissCurrent
        )
        updateState(snackbarData = _snackbarData.copy(type = type))
    }

    private fun updateState(
        page: Page = _page,
        builder: BuilderState = _builder,
        listing: ListingState = _listing,
        snackbarData: SnackbarData = _snackbarData,
    ) {
        _state.update {
            it.copy(
                page = page,
                builder = builder,
                listing = listing,
                snackbarData = snackbarData
            )
        }
    }
}
