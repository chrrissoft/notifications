package com.chrrissoft.notifications.actions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.Util.setBarsColors
import com.chrrissoft.notifications.actions.ActionsEvent.OnChangeBuilder
import com.chrrissoft.notifications.actions.ActionsEvent.OnChangeBuilders
import com.chrrissoft.notifications.actions.ActionsState.ActionBuilder
import com.chrrissoft.notifications.expandable.ExpandableState
import com.chrrissoft.notifications.ui.components.NotificationTextFiled
import com.chrrissoft.notifications.ui.components.TopBarTitle
import com.chrrissoft.notifications.ui.theme.centerAlignedTopAppBarColors
import com.chrrissoft.notifications.ui.theme.filledIconButtonColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsScreen(
    state: ActionsState,
    onEvent: (ActionsEvent) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    setBarsColors(bottom = colorScheme.onPrimary)
    val add = {
        if (state.builder.title.isNotEmpty()) {
            val new = state.builders.plus(state.builder.copy())
            onEvent(OnChangeBuilder(state.builder.copy(title = "")))
            onEvent(OnChangeBuilders(new))
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = centerAlignedTopAppBarColors,
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(imageVector = Rounded.Menu, contentDescription = null)
                    }
                },
                title = { TopBarTitle(title = "Actions Notifications") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { add() }
            ) {
                Icon(imageVector = Rounded.Add, contentDescription = null)
            }
        },
        containerColor = colorScheme.onPrimary,
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp)
            ) {
                ActionBuilderUi(
                    builder = state.builder,
                    onChangeBuilder = { onEvent(OnChangeBuilder(it)) },
                    onAdd = { add() }
                )

                Spacer(modifier = Modifier.height(15.dp))

                Builders(
                    data = state.builders,
                    onDelete = {
                        val builders = state.builders.minus(it)
                        onEvent(OnChangeBuilders(builders))
                    }
                )
            }
        },
    )
}

@Composable
fun Builders(
    data: List<ActionBuilder>,
    onDelete: (ActionBuilder) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(data) {
            NotificationTextFiled(
                enabled = false,
                value = it.title,
                onValueChange = {},
                leadingIcon = {
                    FilledIconButton(
                        onClick = { },
                        shape = shapes.medium,
                        modifier = Modifier.padding(start = 5.dp),
                        colors = filledIconButtonColors,
                    ) {
                        Icon(
                            painter = painterResource(id = it.icon),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        )
                    }
                },
                trailingIcon = {
                    FilledIconButton(
                        onClick = { onDelete(it) },
                        shape = shapes.medium,
                        modifier = Modifier.padding(end = 5.dp),
                        colors = filledIconButtonColors,
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = Rounded.DeleteForever,
                        )
                    }
                },
                label = it.pendingIntentBuilder.label,
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}
