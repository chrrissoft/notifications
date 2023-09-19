package com.chrrissoft.notifications.channels

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.GroupBuilderEvent
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.GroupBuilderEvent.OnChangeState
import com.chrrissoft.notifications.channels.ChannelsState.GroupBuilderState
import com.chrrissoft.notifications.ui.theme.textFieldColors

@Composable
fun ChannelsGroupBuilderPage(
    state: GroupBuilderState,
    onEvent: (GroupBuilderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(10.dp)) {
        TextField(
            value = state.name,
            onValueChange = {
                onEvent(OnChangeState(state.copy(name = it)))
            },
            colors = textFieldColors,
            shape = shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Name") }
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = state.description,
            onValueChange = {
                onEvent(OnChangeState(state.copy(description = it)))
            },
            colors = textFieldColors,
            shape = shapes.medium,
            label = { Text(text = "Description") },
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        )
    }
}
