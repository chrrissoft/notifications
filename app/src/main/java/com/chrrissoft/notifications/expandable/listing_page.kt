package com.chrrissoft.notifications.expandable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MicNone
import androidx.compose.material3.*
import androidx.compose.material3.InputChipDefaults.inputChipBorder
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.expandable.ExpandableEvent.ListingEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.ListingEvent.OnChangeType
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.Builder.*
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.*
import com.chrrissoft.notifications.expandable.ExpandableState.ListingState
import com.chrrissoft.notifications.expandable.listing.Item
import com.chrrissoft.notifications.expandable.listing.Listing
import com.chrrissoft.notifications.ui.theme.inputChipColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableListingPage(
    state: ListingState,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        LazyRow {
            items(Builder.builders.plus(null)) {
                InputChip(
                    selected = it==state.type,
                    label = { Text(text = "${it?.label}") },
                    trailingIcon = { Icon(imageVector = it?.icon ?: Icons.Rounded.MicNone, contentDescription = null) },
                    onClick = { onEvent(OnChangeType(it)) },
                    colors = inputChipColors,
                    border = inputChipBorder(borderColor = colorScheme.onPrimaryContainer)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        when (state.type) {
            Media -> Listing(data = state.listing.filterIsInstance<MediaStyleState>(), onEvent = onEvent)
            Call -> Listing(data = state.listing.filterIsInstance<CallStyleState>(), onEvent = onEvent)
            BigPicture -> Listing(data = state.listing.filterIsInstance<BigPictureStyleState>(), onEvent = onEvent)
            BigText -> Listing(data = state.listing.filterIsInstance<BigTextStyleState>(), onEvent = onEvent)
            Inbox -> Listing(data = state.listing.filterIsInstance<InboxStyleState>(), onEvent = onEvent)
            Messaging -> Listing(data = state.listing.filterIsInstance<MessagingStyleState>(), onEvent = onEvent)
            DecoratedCustomView -> Listing(data = state.listing.filterIsInstance<DecoratedCustomViewStyleState>(), onEvent = onEvent)
            null -> AbstractListing(data = state.listing, onEvent = onEvent)
        }
    }
}

@Composable
fun AbstractListing(
    data: List<AbstractStyleState<*>>,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(data) {
            AbstractItem(data = it, onEvent = onEvent)
        }
    }
}


@Composable
fun AbstractItem(
    data: AbstractStyleState<*>,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (data) {
        is BigPictureStyleState -> Item(data = data, onEvent = onEvent, modifier)
        is BigTextStyleState -> Item(data = data, onEvent = onEvent, modifier)
        is CallStyleState -> Item(data = data, onEvent = onEvent, modifier)
        is DecoratedCustomViewStyleState -> Item(data = data, onEvent = onEvent, modifier)
        is InboxStyleState -> Item(data = data, onEvent = onEvent, modifier)
        is MessagingStyleState -> Item(data = data, onEvent = onEvent, modifier)
        is MediaStyleState -> Item(data = data, onEvent = onEvent, modifier)
    }
}
