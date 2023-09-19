package com.chrrissoft.notifications.actions

import androidx.lifecycle.ViewModel
import com.chrrissoft.notifications.actions.ActionsEvent.OnChangeBuilder
import com.chrrissoft.notifications.actions.ActionsEvent.OnChangeBuilders
import com.chrrissoft.notifications.actions.ActionsState.ActionBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ActionsViewModel @Inject constructor(
    @Module.Mutable
    private val _state: MutableStateFlow<ActionsState>,
    @Module.Immutable
    val stateFlow: StateFlow<ActionsState>
) : ViewModel() {
    val state get() = stateFlow.value
    private val handler = EventHandler()

    fun handleEvent(event: ActionsEvent) {
        event.resolve(handler)
    }

    inner class EventHandler {
        fun onEvent(event: OnChangeBuilder) {
            updateState(builder = event.data)
        }

        fun onEvent(event: OnChangeBuilders) {
            updateState(builders = event.data)
        }

        fun onEvent(event: ActionsEvent.OnChangeState) {
            _state.update { event.data }
        }
    }

    private fun updateState(
        builder: ActionBuilder = state.builder,
        builders: List<ActionBuilder> = state.builders,
    ) {
        _state.update {
            it.copy(builder = builder, builders = builders)
        }
    }
}
