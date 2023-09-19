package com.chrrissoft.notifications.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.notifications.NotificationsEvent.GroupBuilderEvent
import com.chrrissoft.notifications.notifications.NotificationsEvent.GroupBuilderEvent.OnChangeCurrent
import com.chrrissoft.notifications.notifications.NotificationsEvent.GroupBuilderEvent.OnDelete
import com.chrrissoft.notifications.notifications.NotificationsState.GroupBuilderState
import com.chrrissoft.notifications.ui.components.Item
import com.chrrissoft.notifications.ui.theme.filledIconButtonColors
import com.chrrissoft.notifications.ui.theme.textFieldColors

@Composable
fun NotificationGroupBuilderPage(
    state: GroupBuilderState,
    onEvent: (GroupBuilderEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 10.dp)) {
        Spacer(modifier = Modifier.height(10.dp))
        Box {
            val options = remember {
                KeyboardOptions(Sentences, imeAction = Done)
            }
            TextField(
                value = state.current,
                onValueChange = { onEvent(OnChangeCurrent(it)) },
                shape = shapes.medium,
                colors = textFieldColors,
                keyboardOptions = options,
                label = { Text(text = "Name") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(state.set.toList()) {
                Item(name = it, onDelete = { onEvent(OnDelete(it)) })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
