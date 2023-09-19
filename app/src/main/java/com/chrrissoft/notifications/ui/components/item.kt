package com.chrrissoft.notifications.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.ui.theme.filledIconButtonColors
import com.chrrissoft.notifications.ui.theme.textFieldColors

@Composable
fun Item(
    name: String,
    label: String? = null,
    onDelete: () -> Unit,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val labelCompose: (@Composable () -> Unit)? = if (label==null) null else @Composable {
        { Text(text = label) }
    }
    TextField(
        value = name,
        onValueChange = {},
        label = labelCompose,
        enabled = false,
        colors = textFieldColors,
        shape = shapes.medium,
        trailingIcon = {
            FilledIconButton(
                colors = filledIconButtonColors,
                onClick = { onDelete() },
                shape = shapes.medium,
                modifier = Modifier.padding(end = 5.dp)
            ) {
                Icon(imageVector = Icons.Rounded.DeleteForever, contentDescription = null)
            }

        },
        modifier = modifier
            .clickable { onClick?.let { it() } }
            .fillMaxWidth(),
    )
}
