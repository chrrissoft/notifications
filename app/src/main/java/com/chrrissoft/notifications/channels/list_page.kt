package com.chrrissoft.notifications.channels

import android.app.NotificationChannelGroup
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.ListingEvent
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.ListingEvent.*
import com.chrrissoft.notifications.channels.ChannelsState.ListingState
import com.chrrissoft.notifications.channels.Util.resolveImportance
import com.chrrissoft.notifications.channels.Util.resolveVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.FilledIconButton
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
import com.chrrissoft.notifications.ui.components.AlertDialog
import com.chrrissoft.notifications.ui.components.DialogItem
import com.chrrissoft.notifications.ui.theme.filledIconButtonColors


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChannelsListPage(
    state: ListingState,
    onEvent: (ListingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(OnRequest)
        onEvent(OnRequestGroup)
    }

    LazyColumn(modifier = modifier.padding(10.dp)) {
        items(state.channels) { channel ->
            debug("lightColor -> ${channel.lightColor}")
            Channel(
                name = "${channel.name}",
                groups = state.groups,
                importance = channel.importance.resolveImportance.label,
                visibility = channel.lockscreenVisibility.resolveVisibility.label,
                onCopy = { onEvent(OnCopy(channel)) },
                onOpen = { onEvent(OnOpen(channel.id)) },
                onDelete = { onEvent(OnDelete(channel)) },
                onGrouping = { onEvent(OnBindGroup(channel, it)) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun Channel(
    name: String,
    importance: String,
    visibility: String,
    onCopy: () -> Unit,
    onOpen: () -> Unit,
    onDelete: () -> Unit,
    onGrouping: (String?) -> Unit,
    groups: List<NotificationChannelGroup?>,
    modifier: Modifier = Modifier
) {
    var groupDialog by remember {
        mutableStateOf(false)
    }
    if (groupDialog) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        AlertDialog(
            title = "Channel Group",
            text = {
                LazyColumn {
                    items(groups) {
                        DialogItem(
                            text = "${it?.name}",
                            selected = false,
                            onClick = { onGrouping(it?.id) }
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                    }
                }
            },
            onConfirm = { groupDialog = false },
            onDismissRequest = { groupDialog = false }
        )
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colorScheme.primaryContainer)
            .padding(12.dp)
    ) {
        val color = colorScheme.onPrimaryContainer
        // name
        Box {
            Text(
                text = name,
                color = color,
                maxLines = 1,
                overflow = Ellipsis,
                style = typography.titleLarge,
            )
        }
        Row(
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            // info
            Row {
                val style = typography.labelMedium
                Column {
                    Text(text = "Importance:", color = color, style = style)
                    Text(text = "Visibility:", color = color, style = style)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = importance, color = color, style = style)
                    Text(text = visibility, color = color, style = style)
                }
            }

            // actions
            Row {
                FilledIconButton(
                    onClick = { onCopy() },
                    colors = filledIconButtonColors
                ) {
                    Icon(imageVector = Icons.Rounded.CopyAll, contentDescription = null)
                }

                FilledIconButton(
                    onClick = { onOpen() },
                    colors = filledIconButtonColors
                ) {
                    Icon(imageVector = Icons.Rounded.Launch, contentDescription = null)
                }

                FilledIconButton(
                    onClick = { onDelete() },
                    colors = filledIconButtonColors
                ) {
                    Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
                }

                FilledIconButton(
                    onClick = { groupDialog = true },
                    colors = filledIconButtonColors
                ) {
                    Icon(imageVector = Icons.Rounded.GroupAdd, contentDescription = null)
                }
            }
        }
    }
}

private fun debug(message: Any?) {
    Log.d("ChannelsListPage", message.toString())
}
