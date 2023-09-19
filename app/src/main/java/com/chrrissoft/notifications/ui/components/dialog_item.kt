package com.chrrissoft.notifications.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun DialogItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = if (selected) colorScheme.onPrimaryContainer
    else colorScheme.onPrimary

    Row(
        horizontalArrangement = SpaceBetween,
        verticalAlignment = CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(shapes.medium)
            .clickable { onClick() }
            .background(background)
            .padding(10.dp)
    ) {
        val color = if (selected) colorScheme.onPrimary else colorScheme.onPrimaryContainer.copy(.7f)
        Text(text = text, color = color, style = typography.labelMedium)

        val iconColor = if (selected) colorScheme.onPrimary else colorScheme.onPrimary
        Icon(
            tint = iconColor,
            contentDescription = null,
            imageVector = Rounded.Check,
        )
    }
}
