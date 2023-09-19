package com.chrrissoft.notifications.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.notifications.NotificationsEvent.ListingEvent
import com.chrrissoft.notifications.notifications.NotificationsEvent.ListingEvent.*
import com.chrrissoft.notifications.notifications.NotificationsState.ListingState
import com.chrrissoft.notifications.ui.theme.filledIconButtonColors
import com.chrrissoft.notifications.ui.theme.filledIconToggleButtonColors

@Composable
fun NotificationListingPage(
    state: ListingState,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.padding(horizontal = 10.dp)) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(state.list) { item ->
            Item(
                title = item.contentTitle,
                onCopy = { onEvent(OnCopy(item)) },
                onDelete = { onEvent(OnDelete(item)) },
                onLaunch = { onEvent(OnLaunch(item, it)) },
                onEdit = { onEvent(OnEditRequest(item)) },
                onDeleteFromDrawer = { onEvent(OnDeleteFromDrawer(item.id)) },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun Item(
    title: String,
    onCopy: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDeleteFromDrawer: () -> Unit,
    onLaunch: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colorScheme.primaryContainer)
            .padding(12.dp)
    ) {
        // title
        Box {
            Text(
                text = title,
                maxLines = 1,
                overflow = Ellipsis,
                style = typography.titleLarge,
                color = colorScheme.onPrimaryContainer,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween,
        ) {
                var semeId by remember {
                    mutableStateOf(false)
                }

                FilledIconButton(
                    onClick = onEdit,
                    shape = shapes.medium,
                    colors = filledIconButtonColors,
                ) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
                }

                FilledIconButton(
                    onClick = onCopy,
                    shape = shapes.medium,
                    colors = filledIconButtonColors,
                ) {
                    Icon(imageVector = Icons.Rounded.CopyAll, contentDescription = null)
                }

                FilledIconToggleButton(
                    checked = semeId,
                    shape = shapes.medium,
                    onCheckedChange = { semeId = it },
                    colors = filledIconToggleButtonColors
                ) {
                    Icon(imageVector = Icons.Rounded.GeneratingTokens, contentDescription = null)
                }

                FilledIconButton(
                    onClick = { onLaunch(!semeId) },
                    shape = shapes.medium,
                    colors = filledIconButtonColors,
                ) {
                    Icon(imageVector = Icons.Rounded.Launch, contentDescription = null)
                }

                FilledIconButton(
                    onClick = onDeleteFromDrawer,
                    shape = shapes.medium,
                    colors = filledIconButtonColors,
                ) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
                }

                FilledIconButton(
                    onClick = onDelete,
                    shape = shapes.medium,
                    colors = filledIconButtonColors,
                ) {
                    Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
                }
        }
    }
}
