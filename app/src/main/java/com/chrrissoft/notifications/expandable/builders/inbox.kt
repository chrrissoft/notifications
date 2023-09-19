package com.chrrissoft.notifications.expandable.builders

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent.OnChangeState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.InboxStyleState
import com.chrrissoft.notifications.ui.components.NotificationTextFiled

@Composable
fun BuilderContent(
    state: InboxStyleState,
    onEvent: (BuilderEvent) -> Unit,
) {
    NotificationTextFiled(
        value = state.title,
        label = "Title",
        onValueChange = { onEvent(OnChangeState(state.copy(title = it))) },
    )

    Spacer(modifier = Modifier.height(10.dp))

    NotificationTextFiled(
        value = state.summaryText,
        label = "Summary text",
        onValueChange = { onEvent(OnChangeState(state.copy(summaryText = it))) },
    )

    Spacer(modifier = Modifier.height(10.dp))
    LinesBuilder(
        lines = state.lines.toList(),
        onDelete = {
            val lines = state.lines.minus(it)
            onEvent(OnChangeState(state.copy(lines = lines)))
        },
        onAddLine = {
            val lines = state.lines.plus(it.trim())
            onEvent(OnChangeState(state.copy(lines = lines)))
        },
    )
}

@Composable
fun LinesBuilder(
    lines: List<String>,
    onAddLine: (String) -> Unit,
    onDelete: (String) -> Unit,
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    var line by rememberSaveable {
        mutableStateOf("")
    }

    ExpandableBuilder(
        expanded = expanded,
        builder = {
            val add = {
                if (line.isNotEmpty()) {
                    onAddLine(line)
                    line = ""
                }
            }
            val actions = KeyboardActions(onSend = { add() })
            val options = remember {
                KeyboardOptions(Sentences, imeAction = Send)
            }
            NotificationTextFiled(
                value = line,
                onValueChange = { line = it },
                leadingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        val icons = if (!expanded) Rounded.ExpandMore else Rounded.ExpandLess
                        Icon(imageVector = icons, contentDescription = null)
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { add() }) {
                        Icon(imageVector = Rounded.Add, contentDescription = null)
                    }
                },
                keyboardActions = actions,
                keyboardOptions = options,
                maxLines = 1,
            )
        },
        expandedContent = {
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                items(lines) {
                    LineItem(label = it, onDelete = { onDelete(it) })
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        }
    )
}

@Composable
private fun LineItem(
    label: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(MaterialTheme.shapes.medium)
            .background(colorScheme.primaryContainer)
            .padding(10.dp)
    ) {
        Text(
            text = label,
            style = typography.labelMedium,
            color = colorScheme.onPrimaryContainer,
        )
        Icon(
            imageVector = Rounded.Cancel,
            contentDescription = null,
            tint = colorScheme.onPrimaryContainer,
            modifier = Modifier.clickable { onDelete() }
        )
    }
}
