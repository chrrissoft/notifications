package com.chrrissoft.notifications.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import com.chrrissoft.notifications.ui.theme.alertDialogColors
import androidx.compose.material3.AlertDialog as AndroidAlertDialog

@Composable
fun AlertDialog(
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: ImageVector? = null,
    title: String? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    colors: AlertDialogColors = alertDialogColors,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
) {
    AndroidAlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(colors = buttonColors, onClick = { onConfirm() }) {
                Text(text = "Ok")
            }
        },
        modifier = modifier,
        dismissButton = dismissButton,
        icon = {
            if (icon == null) return@AndroidAlertDialog
            Icon(imageVector = icon, contentDescription = null)
        },
        title = {
            if (title == null) return@AndroidAlertDialog
            Text(title)
        },
        text = text,
        shape = shape,
        containerColor = colors.containerColor,
        iconContentColor = colors.iconContentColor,
        titleContentColor = colors.titleContentColor,
        textContentColor = colors.textContentColor,
        tonalElevation = tonalElevation,
        properties = properties,
    )
}

data class AlertDialogColors(
    val containerColor: Color,
    val iconContentColor: Color,
    val titleContentColor: Color,
    val textContentColor: Color,
) {
    companion object {
        @Composable
        fun colors(
            containerColor: Color = AlertDialogDefaults.containerColor,
            iconContentColor: Color = AlertDialogDefaults.iconContentColor,
            titleContentColor: Color = AlertDialogDefaults.titleContentColor,
            textContentColor: Color = AlertDialogDefaults.textContentColor,
        ): AlertDialogColors {
            return AlertDialogColors(
                containerColor,
                iconContentColor,
                titleContentColor,
                textContentColor,
            )
        }
    }
}
