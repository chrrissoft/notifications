package com.chrrissoft.notifications.conversations

import android.app.NotificationManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    private val manager: NotificationManager
) : ViewModel() {
    private val _state = MutableStateFlow(ConversationsState())
    val stateFlow = _state.asStateFlow()
    private val handler = EventHandler()

    fun handleEvent(event: ConversationsEvent) {
        event.resolve(handler)
    }

    inner class EventHandler {
        val space = ConversationsSpaceEventHandler()
        val bubbles = ConversationsInBubblesEventHandler()

        inner class ConversationsSpaceEventHandler

        inner class ConversationsInBubblesEventHandler
    }
}
