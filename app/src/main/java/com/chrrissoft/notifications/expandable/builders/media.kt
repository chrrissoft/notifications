package com.chrrissoft.notifications.expandable.builders

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.MediaStyleState
import com.chrrissoft.notifications.ui.theme.cardColors

@Composable
fun BuilderContent(
    state: MediaStyleState,
    onEvent: (BuilderEvent) -> Unit,
) {

}

@Composable
fun ActionsInCollapsed(
    selectedAction: Set<String>,
    actions: Set<String>,
) {
    var showList by remember {
        mutableStateOf(false)
    }

    ExpandableBuilder(
        expanded = showList,
        builder = {

        },
        expandedContent = {
            LazyColumn {

            }
        }
    )
}
