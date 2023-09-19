package com.chrrissoft.notifications.expandable

import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.*
import com.chrrissoft.notifications.expandable.ExpandableViewModel.EventHandler
import com.chrrissoft.notifications.expandable.ExpandableViewModel.EventHandler.*

sealed interface ExpandableEvent {
    fun resolve(handler: EventHandler) {
        when (this) {
            is BuilderEvent -> resolve(handler.builder)
            is ListingEvent -> resolve(handler.listing)
            is OnChangePage -> handler.onEvent(event = this)
        }
    }

    sealed interface BuilderEvent : ExpandableEvent {
        fun resolve(handler: BuilderEventHandler) {
            when (this) {
                is OnChangeType -> handler.onEvent(event = this)
                is OnChangeState -> handler.onEvent(event = this)
                is OnCreate -> handler.onEvent(event = this)
            }
        }

        data class OnChangeState(val data: AbstractStyleState<*>) : BuilderEvent

        data class OnChangeType(val data: Builder) : BuilderEvent

        data class OnCreate(val data: AbstractStyleState<*>) : BuilderEvent
    }

    sealed interface ListingEvent : ExpandableEvent {
        fun resolve(handler: ListingEventHandler) {
            when (this) {
                is OnCopy -> handler.onEvent(event = this)
                is OnDelete -> handler.onEvent(event = this)
                is OnDeleteFromDrawer -> handler.onEvent(event = this)
                is OnEditRequest -> handler.onEvent(event = this)
                is OnLaunch -> handler.onEvent(event = this)
                is OnChangeType -> handler.onEvent(event = this)
            }
        }

        data class OnDelete(val builder: AbstractStyleState<*>) : ListingEvent

        data class OnEditRequest(val builder: AbstractStyleState<*>) : ListingEvent

        data class OnDeleteFromDrawer(val data: Int) : ListingEvent

        data class OnCopy(val builder: AbstractStyleState<*>) : ListingEvent

        data class OnLaunch(val builder: AbstractStyleState<*>, val semeId: Boolean) : ListingEvent

        data class OnChangeType(val data: Builder?) : ListingEvent
    }

    data class OnChangePage(val data: ExpandableState.Page) : ExpandableEvent
}
