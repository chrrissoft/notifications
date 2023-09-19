package com.chrrissoft.notifications.expandable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.InputChipDefaults.inputChipBorder
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent.*
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.*
import com.chrrissoft.notifications.expandable.builders.BuilderContent
import com.chrrissoft.notifications.ui.theme.inputChipColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableBuilderPage(
    state: BuilderState,
    onEvent: (BuilderEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        LazyRow {
            items(Builder.builders) {
                InputChip(
                    selected = it == state.builder,
                    label = { Text(text = it.label) },
                    trailingIcon = { Icon(imageVector = it.icon, contentDescription = null) },
                    onClick = { onEvent(OnChangeType(it)) },
                    colors = inputChipColors,
                    border = inputChipBorder(borderColor = colorScheme.onPrimaryContainer)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
        Content(state = state, onEvent = onEvent)
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
private fun Content(
    state: BuilderState,
    onEvent: (BuilderEvent) -> Unit,
) {
    when (state.builder) {
        Builder.Media -> {
            BuilderContent(state = state.mediaStyle, onEvent = onEvent)
        }
        Builder.Call -> {
            BuilderContent(state = state.callStyle, onEvent = onEvent)
        }
        Builder.BigPicture -> {
            BuilderContent(state = state.bigPictureStyle, onEvent = onEvent)
        }
        Builder.BigText -> {
            BuilderContent(state = state.bigTextStyle, onEvent = onEvent)
        }
        Builder.Inbox -> {
            BuilderContent(state = state.inboxStyle, onEvent = onEvent)
        }
        Builder.Messaging -> {
            BuilderContent(state = state.messagingStyle, onEvent = onEvent)
        }
        Builder.DecoratedCustomView -> {
            BuilderContent(state = state.decoratedCustomViewStyle, onEvent = onEvent)
        }
    }
}
