package com.chrrissoft.notifications.custom

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CustomViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(CustomState())
    val stateFlow = _state.asStateFlow()
    val state get() = stateFlow.value
    private val handler = EventHandler()

    fun handleEvent(event: CustomEvent) {
        event.resolve(handler)
    }

    inner class EventHandler
}
