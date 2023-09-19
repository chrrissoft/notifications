package com.chrrissoft.notifications.conversations

import com.chrrissoft.notifications.conversations.ConversationsViewModel.EventHandler
import com.chrrissoft.notifications.conversations.ConversationsViewModel.EventHandler.ConversationsInBubblesEventHandler
import com.chrrissoft.notifications.conversations.ConversationsViewModel.EventHandler.ConversationsSpaceEventHandler

sealed interface ConversationsEvent {
    fun resolve(handler: EventHandler) {

    }

    sealed interface ConversationsSpaceEvent {
        fun resolve(handler: ConversationsSpaceEventHandler) {}
    }

    sealed interface ConversationsInBubblesEvent {
        fun resolve(handler: ConversationsInBubblesEventHandler) {}
    }
}
