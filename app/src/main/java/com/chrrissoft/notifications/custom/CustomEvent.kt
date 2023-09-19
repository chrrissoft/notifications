package com.chrrissoft.notifications.custom

sealed interface CustomEvent {
    fun resolve(handler: CustomViewModel.EventHandler) {

    }
}
