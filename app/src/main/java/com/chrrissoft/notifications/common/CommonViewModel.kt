package com.chrrissoft.notifications.common

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CommonState())
    val stateFlow = _state.asStateFlow()
    val state get() = stateFlow.value
    private val handler = EventHandler()

    fun handleEvent(event: CommonEvent) {
        event.resolve(handler)
    }

    inner class EventHandler
}
