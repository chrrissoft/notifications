package com.chrrissoft.notifications.notifications

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Minimize
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.notifications.NotificationsEvent.BuilderEvent.*
import com.chrrissoft.notifications.notifications.NotificationsState.BuilderState.*
import com.chrrissoft.notifications.ui.components.AlertDialog
import com.chrrissoft.notifications.ui.components.DialogItem
import com.chrrissoft.notifications.ui.theme.switchColors
import com.chrrissoft.notifications.ui.theme.textFieldColors
import com.chrrissoft.notifications.ui.theme.textFieldDisableColors
import com.chrrissoft.notifications.notifications.NotificationsEvent.BuilderEvent
import com.chrrissoft.notifications.notifications.NotificationsState.BuilderState
import com.chrrissoft.notifications.shared.*

@Composable
fun NotificationBuilderPage(
    state: BuilderState,
    onEvent: (BuilderEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        onEvent(OnGroupRequest(state))
        onEvent(OnSelfListRequest(state))
        onEvent(OnChannelsListRequest(state))
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        val options = remember {
            KeyboardOptions(Sentences, imeAction = ImeAction.Next)
        }

        // content title
        Box {
            TextField(
                value = state.contentTitle,
                onValueChange = {
                    onEvent(OnChangeBuilder(state.copy(contentTitle = it)))
                },
                shape = shapes.medium,
                colors = textFieldColors,
                keyboardOptions = options,
                label = { Text(text = "Content Title") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // content text
        Box {
            TextField(
                value = state.contentText,
                onValueChange = {
                    onEvent(OnChangeBuilder(state.copy(contentText = it)))
                },
                shape = shapes.medium,
                colors = textFieldColors,
                keyboardOptions = options,
                label = { Text(text = "Content Text") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // content info
        Box {
            TextField(
                value = state.contentInfo,
                onValueChange = {
                    onEvent(OnChangeBuilder(state.copy(contentInfo = it)))
                },
                shape = shapes.medium,
                colors = textFieldColors,
                keyboardOptions = options,
                label = { Text(text = "Content Info") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // sub text
        Box {
            TextField(
                value = state.subText,
                onValueChange = {
                    onEvent(OnChangeBuilder(state.copy(subText = it)))
                },
                shape = shapes.medium,
                colors = textFieldColors,
                keyboardOptions = options,
                label = { Text(text = "Sub Text") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // category & badge
        Row(modifier = Modifier.fillMaxWidth()) {
            var categoryDialog by remember {
                mutableStateOf(false)
            }
            if (categoryDialog) {
                AlertDialog(
                    title = "Category",
                    text = {
                        LazyColumn {
                            items(Category.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.category,
                                    onClick = {
                                        onEvent(OnChangeBuilder(state.copy(category = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { categoryDialog = false },
                    onDismissRequest = { categoryDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = state.category.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                keyboardOptions = options,
                label = { Text(text = "Category") },
                trailingIcon = {
                    Icon(
                        imageVector = state.category.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { categoryDialog = true }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))

            var badgeDialog by remember {
                mutableStateOf(false)
            }
            if (badgeDialog) {
                AlertDialog(
                    title = "Badge Icon Type",
                    text = {
                        LazyColumn {
                            items(BadgeIconType.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = if (SDK_INT<O) it==state.badge else false,
                                    onClick = {
                                        if (SDK_INT<O) return@DialogItem
                                        onEvent(OnChangeBuilder(state.copy(badge = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { badgeDialog = false },
                    onDismissRequest = { badgeDialog = false }
                )
            }
            TextField(
                enabled = SDK_INT>=O,
                value = state.badge.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldDisableColors,
                label = { Text(text = "Badge") },
                trailingIcon = {
                    Icon(
                        imageVector = state.badge.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { badgeDialog = true }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // color & lights
        Row(modifier = Modifier.fillMaxWidth()) {
            var colorDialog by remember {
                mutableStateOf(false)
            }
            if (colorDialog) {
                AlertDialog(
                    title = "Color",
                    text = {
                        LazyColumn {
                            items(Color.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.color,
                                    onClick = {
                                        onEvent(OnChangeBuilder(state.copy(color = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { colorDialog = false },
                    onDismissRequest = { colorDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = state.color.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Color") },
                modifier = Modifier
                    .clickable { colorDialog = true }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))

            var lightsDialog by remember {
                mutableStateOf(false)
            }
            if (lightsDialog) {
                AlertDialog(
                    title = "Lights",
                    text = {
                        Column {
                            Text(text = "Color")
                            Spacer(modifier = Modifier.height(10.dp))
                            (Color.list).forEach {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.lights.color,
                                    onClick = {
                                        val light = state.lights.copy(color = it)
                                        onEvent(
                                            OnChangeBuilder(state.copy(lights = light))
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }

                            Text(text = "onMs")
                            Spacer(modifier = Modifier.height(5.dp))
                            Slider(value = 0f, onValueChange = {})
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(text = "onMs")
                            Spacer(modifier = Modifier.height(5.dp))
                            Slider(value = 0f, onValueChange = {})
                        }
                    },
                    onConfirm = { lightsDialog = false },
                    onDismissRequest = { lightsDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = state.lights.color.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Lights") },
                modifier = Modifier
                    .clickable { lightsDialog = true }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // whenTime & timeoutAfter
        Row(modifier = Modifier.fillMaxWidth()) {
            var whenTimeDialog by remember {
                mutableStateOf(false)
            }
            if (whenTimeDialog) {
                AlertDialog(
                    title = "When Time",
                    text = {
                        Slider(value = 0f, onValueChange = {})
                    },
                    onConfirm = { whenTimeDialog = false },
                    onDismissRequest = { whenTimeDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = "${state.whenTime}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "When Time") },
                modifier = Modifier
                    .clickable { whenTimeDialog = true }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))

            var timeoutAfterDialog by remember {
                mutableStateOf(false)
            }
            if (timeoutAfterDialog) {
                AlertDialog(
                    title = "Timeout After",
                    text = {
                        Slider(value = 0f, onValueChange = {})
                    },
                    onConfirm = { timeoutAfterDialog = false },
                    onDismissRequest = { timeoutAfterDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = "${state.timeoutAfter}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Timeout After") },
                modifier = Modifier
                    .clickable { timeoutAfterDialog = true }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // tap intent & delete intent
        Row(modifier = Modifier.fillMaxWidth()) {
            var tapDialog by remember {
                mutableStateOf(false)
            }
            if (tapDialog) {
                AlertDialog(
                    title = "Content Intent (Tap)",
                    text = {
                        LazyColumn {
                            items(PendingIntentBuilder.list) {
                                DialogItem(
                                    text = "${it?.label}",
                                    selected = it==state.contentIntent,
                                    onClick = {
                                        onEvent(OnChangeBuilder(state.copy(contentIntent = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { tapDialog = false },
                    onDismissRequest = { tapDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = "${state.contentIntent?.label}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Content Intent") },
                trailingIcon = {
                    Icon(
                        imageVector = state.contentIntent?.icon ?: Icons.Rounded.Favorite,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { tapDialog = true }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))

            var deleteDialog by remember {
                mutableStateOf(false)
            }
            if (deleteDialog) {
                AlertDialog(
                    title = "Delete Intent",
                    text = {
                        LazyColumn {
                            items(PendingIntentBuilder.list) {
                                DialogItem(
                                    text = "${it?.label}",
                                    selected = it==state.deleteIntent,
                                    onClick = {
                                        onEvent(
                                            OnChangeBuilder(state.copy(deleteIntent = it))
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { deleteDialog = false },
                    onDismissRequest = { deleteDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = "${state.deleteIntent?.label}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Delete Intent") },
                trailingIcon = {
                    Icon(
                        imageVector = state.deleteIntent?.icon ?: Icons.Rounded.Favorite,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { deleteDialog = true }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // number & groupAlertBehavior
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                enabled = false,
                value = state.numberUi,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Number") },
                leadingIcon = {
                    IconButton(
                        enabled = state.number >= 0,
                        onClick = {
                            onEvent(OnChangeBuilder(state.copy(number = state.number.minus(1))))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Minimize,
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(OnChangeBuilder(state.copy(number = state.number.plus(1))))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))

            var groupAlertBehaviorDialog by remember {
                mutableStateOf(false)
            }
            if (groupAlertBehaviorDialog) {
                AlertDialog(
                    title = "Group Alert Behavior",
                    text = {
                        LazyColumn {
                            items(GroupAlertBehavior.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.groupAlertBehavior,
                                    onClick = {
                                        onEvent(
                                            OnChangeBuilder(state.copy(groupAlertBehavior = it))
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { groupAlertBehaviorDialog = false },
                    onDismissRequest = { groupAlertBehaviorDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = state.groupAlertBehavior.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Badge") },
                trailingIcon = {
                    Icon(
                        imageVector = state.groupAlertBehavior.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { groupAlertBehaviorDialog = true }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // channel
        Box {
            var channelDialog by remember {
                mutableStateOf(false)
            }
            if (channelDialog) {
                AlertDialog(
                    title = "Channel",
                    text = {
                        LazyColumn {
                            items(state.channelsList) {
                                DialogItem(
                                    text = it.second,
                                    selected = it===state.channel,
                                    onClick = {
                                        onEvent(OnChangeBuilder(state.copy(channel = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { channelDialog = false },
                    onDismissRequest = { channelDialog = false }
                )
            }

            TextField(
                enabled = false,
                value = "${state.channel?.first}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Channel") },
                modifier = Modifier
                    .clickable { channelDialog = true }
                    .fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // groups
        Box {
            var groupsDialog by remember {
                mutableStateOf(false)
            }
            if (groupsDialog) {
                AlertDialog(
                    title = "Groups",
                    text = {
                        LazyColumn {
                            items(state.groups.toList()) {
                                DialogItem(
                                    text = "$it",
                                    selected = it==state.group,
                                    onClick = {
                                        onEvent(OnChangeBuilder(state.copy(group = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { groupsDialog = false },
                    onDismissRequest = { groupsDialog = false }
                )
            }

            TextField(
                enabled = false,
                value = "${state.group}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Group") },
                modifier = Modifier
                    .clickable { groupsDialog = true }
                    .fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // publicVersion
        Box {
            var publicVersionDialog by remember {
                mutableStateOf(false)
            }
            if (publicVersionDialog) {
                AlertDialog(
                    title = "Public Version Dialog",
                    text = {
                        LazyColumn {
                            items(state.selfList) {
                                DialogItem(
                                    text = "${it?.contentTitle}",
                                    selected = it===state.publicVersion,
                                    onClick = {
                                        onEvent(OnChangeBuilder(state.copy(publicVersion = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { publicVersionDialog = false },
                    onDismissRequest = { publicVersionDialog = false }
                )
            }

            TextField(
                enabled = false,
                value = "${state.publicVersion?.contentTitle}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Public Version") },
                modifier = Modifier
                    .clickable { publicVersionDialog = true }
                    .fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // auto cancel & silence
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                enabled = false,
                value = "${state.autoCancel}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Auto Cancel") },
                trailingIcon = {
                    Switch(
                        colors = switchColors,
                        checked = state.autoCancel,
                        onCheckedChange = { onEvent(OnChangeBuilder(state.copy(autoCancel = it))) },
                        modifier = Modifier.padding(end = 10.dp),
                    )
                },
                modifier = Modifier
                    .clickable { onEvent(OnChangeBuilder(state.copy(autoCancel = !state.autoCancel))) }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))
            TextField(
                enabled = false,
                value = "${state.silent}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Silent") },
                trailingIcon = {
                    Switch(
                        checked = state.silent,
                        colors = switchColors,
                        onCheckedChange = {
                            onEvent(OnChangeBuilder(state.copy(silent = it)))
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    )
                },
                modifier = Modifier
                    .clickable { onEvent(OnChangeBuilder(state.copy(silent = !state.silent))) }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // show when & generate actions
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                enabled = false,
                value = "${state.showWhen}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Show When") },
                trailingIcon = {
                    Switch(
                        colors = switchColors,
                        checked = state.showWhen,
                        onCheckedChange = { onEvent(OnChangeBuilder(state.copy(showWhen = it))) },
                        modifier = Modifier.padding(end = 10.dp),
                    )
                },
                modifier = Modifier
                    .clickable { onEvent(OnChangeBuilder(state.copy(showWhen = !state.showWhen))) }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))
            TextField(
                enabled = false,
                value = "${state.autoActions}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Auto Actions") },
                trailingIcon = {
                    Switch(
                        checked = state.autoActions,
                        colors = switchColors,
                        onCheckedChange = {
                            onEvent(OnChangeBuilder(state.copy(autoActions = it)))
                        },
                        modifier = Modifier.padding(end = 10.dp)
                    )
                },
                modifier = Modifier
                    .clickable { onEvent(OnChangeBuilder(state.copy(autoActions = !state.autoActions))) }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ongoing & summary
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(
                enabled = false,
                value = "${state.ongoing}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "ongoing") },
                trailingIcon = {
                    Switch(
                        colors = switchColors,
                        checked = state.ongoing,
                        onCheckedChange = { onEvent(OnChangeBuilder(state.copy(ongoing = it))) },
                        modifier = Modifier.padding(end = 10.dp),
                    )
                },
                modifier = Modifier
                    .clickable { onEvent(OnChangeBuilder(state.copy(ongoing = !state.ongoing))) }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))
            TextField(
                enabled = false,
                value = "${state.groupSummary}",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Group Gummary") },
                trailingIcon = {
                    Switch(
                        checked = state.groupSummary,
                        colors = switchColors,
                        onCheckedChange = { onEvent(OnChangeBuilder(state.copy(groupSummary = it))) },
                        modifier = Modifier.padding(end = 10.dp)
                    )
                },
                modifier = Modifier
                    .clickable { onEvent(OnChangeBuilder(state.copy(groupSummary = !state.groupSummary))) }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // vibration & sound
        Row(modifier = Modifier.fillMaxWidth()) {
            var vibrationDialog by remember {
                mutableStateOf(false)
            }
            if (vibrationDialog) {
                AlertDialog(
                    title = "Vibration",
                    text = {
                        LazyColumn {
                            items(Vibration.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.vibration,
                                    onClick = {
                                        onEvent(OnChangeBuilder(state.copy(vibration = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { vibrationDialog = false },
                    onDismissRequest = { vibrationDialog = false }
                )
            }
            TextField(
                enabled = SDK_INT < O,
                value = state.vibration.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldDisableColors,
                label = { Text(text = "Vibration") },
                trailingIcon = {
                    Icon(
                        imageVector = state.vibration.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable(enabled = SDK_INT < O) { vibrationDialog = true }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))

            var soundDialog by remember {
                mutableStateOf(false)
            }
            if (soundDialog) {
                AlertDialog(
                    title = "Sound",
                    text = {
                        LazyColumn {
                            items(Sound.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.sound,
                                    onClick = {
                                        onEvent(
                                            OnChangeBuilder(state.copy(sound = it))
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { soundDialog = false },
                    onDismissRequest = { soundDialog = false }
                )
            }
            TextField(
                enabled = SDK_INT < O,
                value = state.sound.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldDisableColors,
                label = { Text(text = "Sound") },
                trailingIcon = {
                    Icon(
                        imageVector = state.sound.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable(enabled = SDK_INT < O) { soundDialog = true }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // priority & visibility
        Row(modifier = Modifier.fillMaxWidth()) {
            var priorityDialog by remember {
                mutableStateOf(false)
            }
            if (priorityDialog) {
                AlertDialog(
                    title = "Priority",
                    text = {
                        LazyColumn {
                            items(Priority.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.priority,
                                    onClick = {
                                        onEvent(OnChangeBuilder(state.copy(priority = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { priorityDialog = false },
                    onDismissRequest = { priorityDialog = false }
                )
            }
            TextField(
                enabled = SDK_INT < O,
                value = state.priority.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldDisableColors,
                label = { Text(text = "Priority") },
                trailingIcon = {
                    Icon(
                        imageVector = state.priority.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable(enabled = SDK_INT < O) { priorityDialog = true }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))

            var visibilityDialog by remember {
                mutableStateOf(false)
            }
            if (visibilityDialog) {
                AlertDialog(
                    title = "Sound",
                    text = {
                        LazyColumn {
                            items(Visibility.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.visibility,
                                    onClick = {
                                        onEvent(
                                            OnChangeBuilder(state.copy(visibility = it))
                                        )
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { visibilityDialog = false },
                    onDismissRequest = { visibilityDialog = false }
                )
            }
            TextField(
                enabled = SDK_INT < O,
                value = state.visibility.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldDisableColors,
                label = { Text(text = "Visibility") },
                trailingIcon = {
                    Icon(
                        imageVector = state.visibility.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable(enabled = SDK_INT < O) { visibilityDialog = true }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}
