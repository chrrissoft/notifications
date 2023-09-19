package com.chrrissoft.notifications.expandable.listing

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.notifications.expandable.ExpandableEvent.ListingEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.ListingEvent.*
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.MessagingStyleState

@Composable
fun Listing(
    data: List<MessagingStyleState>,
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
    data: MessagingStyleState,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Item(
        title = data.title,
        onCopy = { onEvent(OnCopy(data)) },
        onEdit = { onEvent(OnEditRequest(data)) },
        onDelete = { OnDelete(data) },
        onDeleteFromDrawer = { OnDeleteFromDrawer(0) },
        onLaunch = { onEvent(OnLaunch(data, it)) },
        modifier = modifier,
    )
}
