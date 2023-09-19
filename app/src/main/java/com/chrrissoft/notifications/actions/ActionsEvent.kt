package com.chrrissoft.notifications.actions

import com.chrrissoft.notifications.actions.ActionsState.ActionBuilder
import com.chrrissoft.notifications.actions.ActionsViewModel.EventHandler

sealed interface ActionsEvent {
    fun resolve(handler: EventHandler) {
        when (this) {
            is OnChangeBuilder -> handler.onEvent(event = this)
            is OnChangeBuilders -> handler.onEvent(event = this)
            is OnChangeState -> handler.onEvent(event = this)
        }
    }

    data class OnChangeBuilder(val data: ActionBuilder) : ActionsEvent

    data class OnChangeBuilders(val data: List<ActionBuilder>) : ActionsEvent

    data class OnChangeState(val data: ActionsState) : ActionsEvent
}
