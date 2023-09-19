package com.chrrissoft.notifications.conversations

data class ConversationsState(val foo: String = "") {
    data class ConversationsSpaceState(val foo: String)

    data class ConversationsInBubblesState(val foo: String)
}
