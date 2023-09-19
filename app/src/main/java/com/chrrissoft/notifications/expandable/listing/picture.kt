package com.chrrissoft.notifications.expandable.listing

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.chrrissoft.notifications.expandable.ExpandableEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.ListingEvent
import com.chrrissoft.notifications.expandable.ExpandableState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.BigPictureStyleState

@Composable
fun Listing(
    data: List<BigPictureStyleState>,
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
    data: BigPictureStyleState,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Item(
        title = data.title,
        onCopy = { onEvent(ListingEvent.OnCopy(data)) },
        onEdit = { onEvent(ListingEvent.OnEditRequest(data)) },
        onDelete = { ListingEvent.OnDelete(data) },
        onDeleteFromDrawer = { ListingEvent.OnDeleteFromDrawer(0) },
        onLaunch = { onEvent(ListingEvent.OnLaunch(data, it)) },
        modifier = modifier,
    )
}
