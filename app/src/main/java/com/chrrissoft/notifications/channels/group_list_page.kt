package com.chrrissoft.notifications.channels

import android.app.NotificationChannelGroup
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.GroupListEvent
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.GroupListEvent.*
import com.chrrissoft.notifications.channels.ChannelsState.GroupListState
import com.chrrissoft.notifications.channels.Util.descriptionCompat
import com.chrrissoft.notifications.channels.Util.resolveImportance
import com.chrrissoft.notifications.channels.Util.resolveVisibility
import androidx.compose.material3.IconButtonDefaults.filledIconButtonColors as colors

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChannelsGroupListPage(
    state: GroupListState,
    onEvent: (GroupListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(OnRequest)
    }

    LazyColumn(modifier = modifier.padding(horizontal = 10.dp)) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(state.groups) {
            Group(
                group = it,
                onDelete = { onEvent(OnDelete(it)) },
                onCopy = { onEvent(OnCopy(it)) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun Group(
    group: NotificationChannelGroup,
    onDelete: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colorScheme.primaryContainer)
            .padding(10.dp)
            .animateContentSize()
    ) {
        val color = colorScheme.onPrimaryContainer
        Text(text = "${group.name}", style = typography.titleLarge, color = color)
        Row(
            horizontalArrangement = SpaceBetween,
            verticalAlignment = CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                color = color,
                style = typography.labelMedium,
                text = "Description: ${group.descriptionCompat}...",
            )
            Row {
                FilledIconButton(
                    onClick = { onCopy() },
                    colors = colors(colorScheme.onPrimary, colorScheme.onPrimaryContainer)
                ) {
                    Icon(imageVector = Icons.Rounded.CopyAll, contentDescription = null)
                }
                FilledIconButton(
                    onClick = { onDelete() },
                    colors = colors(colorScheme.onPrimary, colorScheme.onPrimaryContainer)
                ) {
                    Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
                }
                FilledIconButton(
                    onClick = { expanded = !expanded },
//                    enabled = group.channels.isNotEmpty(),
                    colors = colors(colorScheme.onPrimary, colorScheme.onPrimaryContainer)
                ) {
                    val icon = if (!expanded) Icons.Rounded.ExpandMore else Icons.Rounded.ExpandLess
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        }


        if (expanded) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                group.channels.forEach {
                    Channels(name = "${it.name}")
                    Spacer(modifier = Modifier.height(7.dp))
                }
            }
        }
    }
}

@Composable
private fun Channels(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        maxLines = 1,
        overflow = Ellipsis,
        style = typography.titleMedium,
        color = colorScheme.onPrimaryContainer,
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .background(colorScheme.onPrimary)
            .padding(10.dp),
    )
}
