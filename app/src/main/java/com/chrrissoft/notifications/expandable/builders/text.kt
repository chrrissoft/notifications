package com.chrrissoft.notifications.expandable.builders

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent.OnChangeState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.BigTextStyleState
import com.chrrissoft.notifications.ui.components.NotificationTextFiled

@Composable
fun BuilderContent(
    state: BigTextStyleState,
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

    NotificationTextFiled(
        value = state.text,
        label = "Text",
        onValueChange = { onEvent(OnChangeState(state.copy(text = it))) },
    )
}
