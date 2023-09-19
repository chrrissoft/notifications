package com.chrrissoft.notifications.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.material3.InputChipDefaults.inputChipBorder
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.actions.ActionsState.ActionBuilder
import com.chrrissoft.notifications.shared.PendingIntentBuilder
import com.chrrissoft.notifications.shared.PendingIntentBuilder.Companion.nonNullList
import com.chrrissoft.notifications.ui.components.NotificationTextFiled
import com.chrrissoft.notifications.ui.theme.filledIconToggleButtonColors
import com.chrrissoft.notifications.ui.theme.inputChipColors

@Composable
fun ActionBuilderUi(
    builder: ActionBuilder,
    onChangeBuilder: (ActionBuilder) -> Unit,
    onAdd: () -> Unit,
) {
    PendingIntentChooser(
        selected = builder.pendingIntentBuilder,
        onChangeSelected = { onChangeBuilder(builder.copy(pendingIntentBuilder = it)) },
    )
    Spacer(modifier = Modifier.height(10.dp))
    NotificationTextFiled(
        value = builder.title,
        onValueChange = {
            onChangeBuilder(builder.copy(title = it))
        },
        singleLine = true,
        label = "Title",
        keyboardActions = KeyboardActions { onAdd() }
    )
    Spacer(modifier = Modifier.height(10.dp))
    IconChooser(
        icons = actionIcons,
        selected = builder.icon,
        onSelect = { onChangeBuilder(builder.copy(icon = it)) },
    )
}

@Composable
fun IconChooser(
    selected: Int,
    onSelect: (Int) -> Unit,
    icons: List<Int>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(shapes.medium)
            .background(colorScheme.primaryContainer)
            .padding(10.dp)
    ) {
        Text(
            text = "Icons chooser",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            items(icons) {
                Icon(
                    icon = it,
                    selected = it==selected,
                    onClick = { onSelect(it) }
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
private fun Icon(
    icon: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledIconToggleButton(
        checked = selected,
        onCheckedChange = { onClick() },
        colors = filledIconToggleButtonColors,
        shape = shapes.medium,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingIntentChooser(
    selected: PendingIntentBuilder,
    onChangeSelected: (PendingIntentBuilder) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        nonNullList.forEach {
            InputChip(
                selected = selected===it,
                label = { Text(text = it.label) },
                onClick = { onChangeSelected(it) },
                colors = inputChipColors,
                border = inputChipBorder(colorScheme.onPrimaryContainer)
            )
        }
    }
}
