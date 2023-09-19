package com.chrrissoft.notifications.expandable.listing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.ui.theme.filledIconButtonColors
import com.chrrissoft.notifications.ui.theme.filledIconToggleButtonColors

@Composable
fun Item(
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
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
    ) {
        // title
        Box {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            var semeId by remember {
                mutableStateOf(false)
            }

            FilledIconButton(
                onClick = onEdit,
                shape = MaterialTheme.shapes.medium,
                colors = filledIconButtonColors,
            ) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
            }

            FilledIconButton(
                onClick = onCopy,
                shape = MaterialTheme.shapes.medium,
                colors = filledIconButtonColors,
            ) {
                Icon(imageVector = Icons.Rounded.CopyAll, contentDescription = null)
            }

            FilledIconToggleButton(
                checked = semeId,
                shape = MaterialTheme.shapes.medium,
                onCheckedChange = { semeId = it },
                colors = filledIconToggleButtonColors
            ) {
                Icon(imageVector = Icons.Rounded.GeneratingTokens, contentDescription = null)
            }

            FilledIconButton(
                onClick = { onLaunch(!semeId) },
                shape = MaterialTheme.shapes.medium,
                colors = filledIconButtonColors,
            ) {
                Icon(imageVector = Icons.Rounded.Launch, contentDescription = null)
            }

            FilledIconButton(
                onClick = onDeleteFromDrawer,
                shape = MaterialTheme.shapes.medium,
                colors = filledIconButtonColors,
            ) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = null)
            }

            FilledIconButton(
                onClick = onDelete,
                shape = MaterialTheme.shapes.medium,
                colors = filledIconButtonColors,
            ) {
                Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
            }
        }
    }
}
