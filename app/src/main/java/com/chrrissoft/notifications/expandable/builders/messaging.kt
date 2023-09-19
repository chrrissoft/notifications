package com.chrrissoft.notifications.expandable.builders

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction.Companion.Send
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Words
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent.OnChangeState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.MessagingStyleState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.MessagingStyleState.MessageBuilder
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.MessagingStyleState.PersonBuilder
import com.chrrissoft.notifications.ui.components.Item
import com.chrrissoft.notifications.ui.components.NotificationTextFiled
import com.chrrissoft.notifications.ui.theme.filledIconButtonColors
import kotlin.math.roundToInt

@Composable
fun BuilderContent(
    state: MessagingStyleState,
    onEvent: (BuilderEvent) -> Unit,
) {
    val options = remember {
        KeyboardOptions(Sentences)
    }
    NotificationTextFiled(
        value = state.title,
        label = "Title",
        keyboardOptions = options,
        onValueChange = { onEvent(OnChangeState(state.copy(title = it))) },
    )
    Spacer(modifier = Modifier.height(10.dp))
    MessageBuilderUi(
        persons = state.personBuilder.persons,
        message = state.message,
        onChangeMessage = { onEvent(OnChangeState(state.copy(message = it))) },
    )
    Spacer(modifier = Modifier.height(10.dp))
    PersonBuilderUi(
        person = state.personBuilder,
        onChangePerson = { onEvent(OnChangeState(state.copy(personBuilder = it))) }
    )
}

@Composable
private fun MessageBuilderUi(
    message: MessageBuilder,
    persons: Set<PersonBuilder>,
    onChangeMessage: (MessageBuilder) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showListing by remember {
        mutableStateOf(false)
    }

    ExpandableBuilder(
        expanded = showListing,
        builder = {
            val add = {
                val messages = message.messages.plus(message.copy(messages = emptySet()))
                val new = message.copy(text = "", messages = messages)
                onChangeMessage(new)
            }

            val options = remember {
                KeyboardOptions(Sentences)
            }
            NotificationTextFiled(
                value = message.text,
                label = "Text",
                leadingIcon = {
                    FilledIconButton(
                        colors = filledIconButtonColors,
                        shape = shapes.medium,
                        modifier = Modifier.padding(start = 5.dp),
                        onClick = { showListing = !showListing }
                    ) {
                        val icon = if (showListing) Rounded.ExpandLess else Rounded.ExpandMore
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                trailingIcon = {
                    FilledIconButton(
                        onClick = { add() },
                        colors = filledIconButtonColors,
                        shape = shapes.medium,
                        modifier = Modifier.padding(end = 5.dp),
                    ) {
                        Icon(imageVector = Rounded.Add, contentDescription = null)
                    }
                },
                keyboardOptions = options,
                onValueChange = { onChangeMessage(message.copy(text = it)) },
            )

            Spacer(modifier = Modifier.height(10.dp))

            Slider(
                value = 0f,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.medium)
                    .background(colorScheme.primaryContainer)
                    .padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            var showPersons by remember {
                mutableStateOf(false)
            }
            ExpandableBuilder(
                expanded = showPersons,
                builder = {
                    NotificationTextFiled(
                        value = message.personBuilder.name,
                        label = "Selected Person",
                        leadingIcon = {
                            FilledIconButton(
                                colors = filledIconButtonColors,
                                shape = shapes.medium,
                                modifier = Modifier.padding(start = 5.dp),
                                onClick = { showPersons = !showPersons }
                            ) {
                                val icon =
                                    if (showPersons) Rounded.ExpandLess else Rounded.ExpandMore
                                Icon(imageVector = icon, contentDescription = null)
                            }
                        },
                        enabled = false,
                        onValueChange = { },
                    )
                },
                expandedContent = {
                    LazyColumn {
                        item {
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                        items(persons.toList()) {
                            SelectableItem(
                                label = it.name,
                                selected = it==message.personBuilder,
                                onClick = { onChangeMessage(message.copy(personBuilder = it)) },
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                }
            )
        },
        expandedContent = {
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(message.messages.toList()) {
                    Item(
                        name = it.text,
                        label = "Person: ${it.personBuilder.name}",
                        onDelete = {
                            val messages = message.messages.minus(it)
                            onChangeMessage(message.copy(messages = messages))
                        },
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        },
        modifier = modifier,
        maxHeight = LocalConfiguration.current.screenHeightDp.dp
    )
}

@Composable
private fun PersonBuilderUi(
    person: PersonBuilder,
    onChangePerson: (PersonBuilder) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showListing by remember {
        mutableStateOf(false)
    }

    ExpandableBuilder(
        expanded = showListing,
        builder = {
            val add = {
                if (person.name.isNotEmpty()) {
                    val persons = person.persons.plus(person.copy(persons = emptySet()))
                    val new = person.copy(name = "", persons = persons)
                    onChangePerson(new)
                }
            }

            val options = remember {
                KeyboardOptions(Words, imeAction = Send)
            }
            NotificationTextFiled(
                value = person.name,
                label = "Person name",
                leadingIcon = {
                    FilledIconButton(
                        colors = filledIconButtonColors,
                        modifier = Modifier.padding(start = 5.dp),
                        shape = shapes.medium,
                        onClick = { showListing = !showListing }
                    ) {
                        val icon = if (showListing) Rounded.ExpandLess else Rounded.ExpandMore
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                trailingIcon = {
                    FilledIconButton(
                        onClick = { add() },
                        colors = filledIconButtonColors,
                        shape = shapes.medium,
                        modifier = Modifier.padding(end = 5.dp),
                    ) {
                        Icon(imageVector = Rounded.Add, contentDescription = null)
                    }
                },
                keyboardOptions = options,
                keyboardActions = KeyboardActions(onSend = { add() }),
                onValueChange = { onChangePerson(person.copy(name = it)) },
            )
        },
        expandedContent = {
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(person.persons.toList()) {
                    SelectableItem(
                        label = it.name,
                        selected = person.selection===it,
                        onClick = {
                            val new = person.copy(selection = it)
                            onChangePerson(new)
                        },
                        onDelete = {
                            val persons = person.persons.minus(it)
                            val new = person.copy(persons = persons)
                            onChangePerson(new)
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun ExpandableBuilder(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    builder: @Composable () -> Unit,
    expandedContent: @Composable () -> Unit,
    maxHeight: Dp = LocalConfiguration.current.screenHeightDp.div(3).dp,
) {
    val background = if (expanded) colorScheme.primaryContainer.copy((.3f)) else Transparent
    val padding = if (expanded) 10.dp else 0.dp

    Column(
        modifier = modifier
            .heightIn(max = maxHeight)
            .clip(shapes.medium)
            .background(background)
            .padding(padding)
            .animateContentSize()
    ) {
        builder()
        if (expanded) {
            expandedContent()
        }
    }
}

@Composable
private fun SelectableItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val background = if (selected) colorScheme.onPrimaryContainer else colorScheme.primaryContainer

    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clip(shapes.medium)
            .clickable { onClick() }
            .background(background)
            .padding(10.dp)
    ) {
        val color = if (selected) colorScheme.onPrimary else colorScheme.onPrimaryContainer
        Text(
            text = label,
            style = typography.labelMedium,
            color = color,
        )
        if (selected) {
            Icon(
                imageVector = Rounded.Done,
                contentDescription = null,
                tint = colorScheme.onPrimary
            )
        } else {
            Box {
                onDelete?.let {
                    Icon(
                        imageVector = Rounded.Cancel,
                        contentDescription = null,
                        tint = colorScheme.onPrimaryContainer,
                        modifier = Modifier.clickable { it() }
                    )
                }
            }
        }
    }
}
