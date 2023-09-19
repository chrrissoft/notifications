package com.chrrissoft.notifications.notifications

import com.chrrissoft.notifications.notifications.NotificationsState.Page
import com.chrrissoft.notifications.notifications.NotificationsViewModel.EventHandler
import com.chrrissoft.notifications.notifications.NotificationsState.BuilderState as BuilderState
import com.chrrissoft.notifications.notifications.NotificationsViewModel.EventHandler.BuilderEventHandler as BuilderEventHandler
import com.chrrissoft.notifications.notifications.NotificationsViewModel.EventHandler.GroupBuilderEventHandler as GroupBuilderEventHandler
import com.chrrissoft.notifications.notifications.NotificationsViewModel.EventHandler.ListingEventHandler as ListingEventHandler

sealed interface NotificationsEvent {
    fun resolve(handler: EventHandler) {
        when (this) {
            is BuilderEvent -> resolve(handler = handler.builder)
            is ListingEvent -> resolve(handler = handler.listing)
            is GroupBuilderEvent -> resolve(handler = handler.groupBuilder)
            is OnChangePage -> handler.onEvent(event = this)
            is OnChangeState -> handler.onEvent(event = this)
        }
    }

    sealed interface BuilderEvent : NotificationsEvent {
        fun resolve(handler: BuilderEventHandler) {
            when (this) {
                is OnCreate -> handler.onEvent(event = this)
                is OnChangeBuilder -> handler.onEvent(event = this)
                is OnGroupRequest -> handler.onEvent(event = this)
                is OnSelfListRequest -> handler.onEvent(event = this)
                is OnChannelsListRequest -> handler.onEvent(event = this)
                is OnEdit -> handler.onEvent(event = this)
            }
        }

        data class OnCreate(val data: BuilderState) : BuilderEvent

        data class OnEdit(val data: BuilderState) : BuilderEvent

        data class OnChangeBuilder(val data: BuilderState) : BuilderEvent

        data class OnGroupRequest(val data: BuilderState) : BuilderEvent

        data class OnSelfListRequest(val data: BuilderState) : BuilderEvent

        data class OnChannelsListRequest(val data: BuilderState) : BuilderEvent
    }

    sealed interface ListingEvent : NotificationsEvent {
        fun resolve(handler: ListingEventHandler) {
            when (this) {
                is OnDelete -> handler.onEvent(event = this)
                is OnCopy -> handler.onEvent(event = this)
                is OnLaunch -> handler.onEvent(event = this)
                is OnEditRequest -> handler.onEvent(event = this)
                is OnDeleteFromDrawer -> handler.onEvent(event = this)
            }
        }

        data class OnDelete(val data: BuilderState) : ListingEvent

        data class OnDeleteFromDrawer(val data: Int) : ListingEvent

        data class OnCopy(val data: BuilderState) : ListingEvent

        data class OnEditRequest(val data: BuilderState) : ListingEvent

        data class OnLaunch(val data: BuilderState, val semeId: Boolean) : ListingEvent
    }

    sealed interface GroupBuilderEvent : NotificationsEvent {
        fun resolve(handler: GroupBuilderEventHandler) {
            when (this) {
                is OnCreate -> handler.onEvent(event = this)
                is OnDelete -> handler.onEvent(event = this)
                is OnChangeCurrent -> handler.onEvent(event = this)
            }
        }

        data class OnCreate(val data: String) : GroupBuilderEvent

        data class OnDelete(val data: String) : GroupBuilderEvent

        data class OnChangeCurrent(val data: String) : GroupBuilderEvent
    }

    data class OnChangePage(val data: Page) : NotificationsEvent

    data class OnChangeState(val data: NotificationsState) : NotificationsEvent
}
