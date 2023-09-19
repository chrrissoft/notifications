package com.chrrissoft.notifications.common

sealed interface CommonEvent {
    fun resolve(handler: CommonViewModel.EventHandler) {

    }
}
