package com.chrrissoft.notifications.expandable.builders

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Picture
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent
import com.chrrissoft.notifications.expandable.ExpandableEvent.BuilderEvent.OnChangeState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.BigPictureStyleState
import com.chrrissoft.notifications.ui.components.NotificationTextFiled

@Composable
fun BuilderContent(
    state: BigPictureStyleState,
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

    val listing = listOf(
        createBitmap(9, 9, Bitmap.Config.ALPHA_8),
        createBitmap(9, 9, Bitmap.Config.ALPHA_8),
        createBitmap(9, 9, Bitmap.Config.ALPHA_8),
        createBitmap(9, 9, Bitmap.Config.ALPHA_8),
    )
    val selectedBitmap = remember {
        mutableStateOf(listing.first())
    }

    Spacer(modifier = Modifier.height(10.dp))
    PictureChooser(
        title = "Big Picture",
        picture = selectedBitmap.value,
        onChangePicture = { onEvent(OnChangeState(state.copy(largeBitmap = it))) },
        pictures = listing
    )

    Spacer(modifier = Modifier.height(10.dp))
    PictureChooser(
        title = "Small Picture",
        picture = selectedBitmap.value,
        onChangePicture = { onEvent(OnChangeState(state.copy(bitmap = it))) },
        pictures = listing
    )

    Spacer(modifier = Modifier.height(10.dp))
    NotificationTextFiled(
        value = state.description,
        label = "Description",
        onValueChange = { onEvent(OnChangeState(state.copy(description = it))) },
    )
}

@Composable
fun PictureChooser(
    title: String,
    picture: Bitmap,
    onChangePicture: (Bitmap) -> Unit,
    pictures: List<Bitmap>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(shapes.medium)
            .background(colorScheme.primaryContainer)
            .padding(10.dp)
    ) {
        Text(text = title, style = typography.titleMedium, color = colorScheme.onPrimaryContainer)
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow {
            items(pictures) {
                Picture(
                    bitmap = it,
                    selected = it===picture,
                    onClick = { onChangePicture(it) }
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
private fun Picture(
    bitmap: Bitmap,
    selected: Boolean,
    size: Dp = LocalConfiguration.current.screenWidthDp.div(4).dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = modifier
            .size(size)
            .clip(shapes.medium)
            .clickable { onClick() }
            .border(1.dp, colorScheme.onPrimaryContainer, shape = shapes.medium)
    )
}
