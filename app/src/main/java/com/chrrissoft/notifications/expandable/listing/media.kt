package com.chrrissoft.notifications.expandable.listing

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.notifications.expandable.ExpandableEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.ListingEvent
import com.chrrissoft.notifications.expandable.ExpandableState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.MediaStyleState

@Composable
fun Listing(
    data: List<MediaStyleState>,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(data) {
            Item(data = it, onEvent = onEvent)
        }
    }
}


@Composable
fun Item(
    data: MediaStyleState,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Item(
        title = "",
        onCopy = { onEvent(ListingEvent.OnCopy(data)) },
        onEdit = { onEvent(ListingEvent.OnEditRequest(data)) },
        onDelete = { ListingEvent.OnDelete(data) },
        onDeleteFromDrawer = { ListingEvent.OnDeleteFromDrawer(0) },
        onLaunch = { onEvent(ListingEvent.OnLaunch(data, it)) },
        modifier = modifier,
    )
}
