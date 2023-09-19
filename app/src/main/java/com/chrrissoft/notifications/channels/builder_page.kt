package com.chrrissoft.notifications.channels

import android.media.MediaPlayer
import android.os.Build
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.Util.play
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.BuilderEvent
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.BuilderEvent.OnChange
import com.chrrissoft.notifications.channels.ChannelsScreenEvent.BuilderEvent.OnRequestChannelGroups
import com.chrrissoft.notifications.channels.ChannelsState.BuilderState
import com.chrrissoft.notifications.channels.ChannelsState.BuilderState.Importance
import com.chrrissoft.notifications.shared.Color
import com.chrrissoft.notifications.shared.Sound
import com.chrrissoft.notifications.shared.Vibration
import com.chrrissoft.notifications.shared.Visibility
import com.chrrissoft.notifications.ui.components.AlertDialog
import com.chrrissoft.notifications.ui.components.DialogItem
import com.chrrissoft.notifications.ui.theme.switchColors
import com.chrrissoft.notifications.ui.theme.textFieldColors

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun ChannelsBuilderPage(
    state: BuilderState,
    onEvent: (BuilderEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        onEvent(OnRequestChannelGroups)
    }

    val ctx = LocalContext.current
    val vibrator = remember {
        ctx.getSystemService(Vibrator::class.java)
    }

    val player = remember {
        MediaPlayer()
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        val options = remember {
            KeyboardOptions(Sentences, imeAction = Done)
        }
        TextField(
            value = state.name,
            onValueChange = {
                onEvent(OnChange(state.copy(name = it)))
            },
            shape = shapes.medium,
            colors = textFieldColors,
            keyboardOptions = options,
            label = { Text(text = "Name") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(10.dp))

        // importance & visibility
        Row(modifier = Modifier.fillMaxWidth()) {
            var importanceDialog by remember {
                mutableStateOf(false)
            }
            if (importanceDialog) {
                AlertDialog(
                    title = "Importance",
                    text = {
                        LazyColumn {
                            items(Importance.list) {
                                DialogItem(
                                    text = it.label,
                                    selected = it==state.importance,
                                    onClick = {
                                        onEvent(OnChange(state.copy(importance = it)))
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { importanceDialog = false },
                    onDismissRequest = { importanceDialog = false }
                )
            }

            TextField(
                enabled = false,
                value = state.importance.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Importance") },
                trailingIcon = {
                    Icon(
                        imageVector = state.importance.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { importanceDialog = true }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))

            var visibilityDialog by remember {
                mutableStateOf(false)
            }
            if (visibilityDialog) {
                AlertDialog(
                    title = "Visibility",
                    text = {
                        Column {
                            Text(
                                text = "Visibility is only modifiable in channels settings",
                                color = colorScheme.onPrimaryContainer,
                                style = typography.labelMedium
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            LazyColumn {
                                items(Visibility.list) {
                                    DialogItem(
                                        text = it.label,
                                        selected = it==state.visibility,
                                        onClick = {}
                                    )
                                    Spacer(modifier = Modifier.height(7.dp))
                                }
                            }
                        }
                    },
                    onConfirm = { visibilityDialog = false },
                    onDismissRequest = { visibilityDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = state.visibility.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Visibility") },
                trailingIcon = {
                    Icon(
                        imageVector = state.visibility.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { visibilityDialog = true }
                    .weight(1f)
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
                                        onEvent(OnChange(state.copy(vibration = it)))
                                        if (it==Vibration.NONE || it==Vibration.DEFAULT)
                                            return@DialogItem
                                        vibrator.vibrate(it.original, (-1))
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
                enabled = false,
                value = state.vibration.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Vibration") },
                trailingIcon = {
                    Icon(
                        imageVector = state.vibration.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { vibrationDialog = true }
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
                                    selected = it==state.audio,
                                    onClick = {
                                        onEvent(OnChange(state.copy(audio = it)))
                                        if (it==Sound.DEFAULT) return@DialogItem
                                        player.play(ctx, it.playerUri)
                                        debug(player.isPlaying)
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
                enabled = false,
                value = state.audio.label,
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Sound") },
                trailingIcon = {
                    Icon(
                        imageVector = state.audio.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .clickable { soundDialog = true }
                    .weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // group & color
        Row(modifier = Modifier.fillMaxWidth()) {
            var groupDialog by remember {
                mutableStateOf(false)
            }
            if (groupDialog) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
                AlertDialog(
                    title = "Channel Group",
                    text = {
                        LazyColumn {
                            items(state.groups) {
                                DialogItem(
                                    text = "${it?.name}",
                                    selected = it?.id==state.group?.first,
                                    onClick = {
                                        if (it == null) {
                                            onEvent(OnChange(state.copy(group = null)))
                                        } else {
                                            val newPair = Pair(it.id, "${it.name}")
                                            onEvent(OnChange(state.copy(group = newPair)))
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(7.dp))
                            }
                        }
                    },
                    onConfirm = { groupDialog = false },
                    onDismissRequest = { groupDialog = false }
                )
            }
            TextField(
                enabled = false,
                value = state.group?.second ?: "Null",
                onValueChange = {},
                shape = shapes.medium,
                colors = textFieldColors,
                label = { Text(text = "Group") },
                modifier = Modifier
                    .clickable { groupDialog = true }
                    .weight(1f),
            )
            Spacer(modifier = Modifier.weight(.05f))
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
                                        onEvent(OnChange(state.copy(color = it)))
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
        }

        Spacer(modifier = Modifier.height(10.dp))

        // show badge
        TextField(
            value = "Show badge",
            onValueChange = {},
            enabled = false,
            trailingIcon = {
                Switch(
                    checked = state.showBadge,
                    onCheckedChange = {
                        onEvent(OnChange(state.copy(showBadge = it)))
                    },
                    colors = switchColors,
                    modifier = Modifier.padding(end = 10.dp)
                )
            },
            shape = shapes.medium,
            colors = textFieldColors,
            modifier = Modifier
                .clickable {
                    onEvent(OnChange(state.copy(showBadge = !state.showBadge)))
                }
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            shape = shapes.medium,
            value = state.description,
            colors = textFieldColors,
            label = { Text(text = "Description") },
            onValueChange = { onEvent(OnChange(state.copy(description = it))) },
            modifier = Modifier.fillMaxSize(),
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}

private fun debug(message: Any?) {
    Log.d("ChannelsBuilderPage", message.toString())
}
