package com.chrrissoft.notifications.channels

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.os.Build
import androidx.annotation.RequiresApi
import com.chrrissoft.notifications.channels.ChannelsState.*
import com.chrrissoft.notifications.channels.ChannelsViewModel.EventHandler
import com.chrrissoft.notifications.channels.ChannelsViewModel.EventHandler.*

@RequiresApi(Build.VERSION_CODES.O)
sealed interface ChannelsScreenEvent {
    fun resolve(handler: EventHandler) {
        when (this) {
            is BuilderEvent -> resolve(handler.builderHandler)
            is ListingEvent -> resolve(handler.listHandler)
            is GroupBuilderEvent -> resolve(handler.groupBuilderHandler)
            is GroupListEvent -> resolve(handler.groupListHandler)
            is OnChangePage -> handler.onEvent(event = this)
            is OnChangeState -> handler.onEvent(event = this)
        }
    }

    sealed interface BuilderEvent : ChannelsScreenEvent {
        fun resolve(handler: BuilderHandler) {
            when (this) {
                is OnChange -> handler.onEvent(event = this)
                is OnCreate -> handler.onEvent(event = this)
                OnRequestChannelGroups -> handler.onRequestGroups()
            }
        }

        data class OnChange(val data: BuilderState) : BuilderEvent

        data class OnCreate(val data: BuilderState) : BuilderEvent

        object OnRequestChannelGroups : BuilderEvent
    }

    sealed interface ListingEvent : ChannelsScreenEvent {
        fun resolve(handler: ListHandler) {
            when (this) {
                is OnCopy -> handler.onEvent(event = this)
                is OnDelete -> handler.onEvent(event = this)
                is OnOpen -> handler.onEvent(event = this)
                is OnBindGroup -> handler.onEvent(event = this)
                OnRequest -> handler.onRequest()
                OnRequestGroup -> handler.onRequestGroups()
            }
        }

        data class OnOpen(val data: String) : ListingEvent

        data class OnCopy(val data: NotificationChannel) : ListingEvent

        data class OnDelete(val data: NotificationChannel) : ListingEvent

        data class OnBindGroup(val data: NotificationChannel, val id: String?) : ListingEvent

        object OnRequest : ListingEvent

        object OnRequestGroup : ListingEvent
    }

    sealed interface GroupBuilderEvent : ChannelsScreenEvent {
        fun resolve(handler: GroupBuilderHandler) {
            when (this) {
                is OnChangeState -> handler.onEvent(event = this)
                is OnCreate -> handler.onEvent(event = this)
            }
        }

        data class OnChangeState(val data: GroupBuilderState) : GroupBuilderEvent

        data class OnCreate(val data: GroupBuilderState) : GroupBuilderEvent
    }

    sealed interface GroupListEvent : ChannelsScreenEvent {
        fun resolve(handler: GroupListHandler) {
            when (this) {
                is OnCopy -> handler.onEvent(event = this)
                is OnDelete -> handler.onEvent(event = this)
                OnRequest -> handler.onRequest()
            }
        }

        data class OnCopy(val data: NotificationChannelGroup) : GroupListEvent

        data class OnDelete(val data: NotificationChannelGroup) : GroupListEvent

        object OnRequest : GroupListEvent
    }

    data class OnChangePage(val data: Page) : ChannelsScreenEvent

    data class OnChangeState(val data: ChannelsState) : ChannelsScreenEvent
}
