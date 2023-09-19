package com.chrrissoft.notifications.ui.theme

import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.InputChipDefaults.inputChipColors
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.chrrissoft.notifications.ui.components.AlertDialogColors
import androidx.compose.material3.NavigationBarItemDefaults.colors as navigationBarItemColors

@OptIn(ExperimentalMaterial3Api::class)
val centerAlignedTopAppBarColors
    @Composable get() = run {
        centerAlignedTopAppBarColors(
            containerColor = colorScheme.primaryContainer,
            navigationIconContentColor = colorScheme.primary,
            titleContentColor = colorScheme.primary,
            actionIconContentColor = colorScheme.primary,
        )
    }

val navigationBarItemColors
    @Composable get() = run {
        navigationBarItemColors(
            selectedIconColor = colorScheme.onPrimary,
            selectedTextColor = colorScheme.primary,
            indicatorColor = colorScheme.primary,
            unselectedIconColor = colorScheme.primary.copy(.5f),
            unselectedTextColor = colorScheme.primary.copy(.5f),
        )
    }

val navigationDrawerItemColors
    @Composable get() = run {
        NavigationDrawerItemDefaults.colors(
            selectedContainerColor = colorScheme.primary,
            unselectedContainerColor = colorScheme.onPrimary,
            selectedIconColor = colorScheme.onPrimary,
            unselectedIconColor = colorScheme.secondary.copy(.5f),
            selectedTextColor = colorScheme.onPrimary,
            unselectedTextColor = colorScheme.secondary.copy(.5f),
        )
    }

@OptIn(ExperimentalMaterial3Api::class)
val inputChipColors
    @Composable get() = run {
        inputChipColors(
            containerColor = colorScheme.primaryContainer,
            labelColor = colorScheme.primary,
            leadingIconColor = colorScheme.primary,
            trailingIconColor = colorScheme.primary,
            disabledContainerColor = colorScheme.primaryContainer.copy(.5f),
            disabledLabelColor = colorScheme.secondary.copy(.5f),
            disabledLeadingIconColor = colorScheme.secondary.copy(.5f),
            disabledTrailingIconColor = colorScheme.secondary.copy(.5f),
            selectedContainerColor = colorScheme.primary,
            disabledSelectedContainerColor = colorScheme.errorContainer,
            selectedLabelColor = colorScheme.onPrimary,
            selectedLeadingIconColor = colorScheme.onPrimary,
            selectedTrailingIconColor = colorScheme.onPrimary,
        )
    }

val cardColors
    @Composable get() = run {
        cardColors(
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer,
        )
    }

val textFieldColors
    @Composable get() = run {
        TextFieldDefaults.colors(
            focusedTextColor = colorScheme.primary,
            unfocusedTextColor = colorScheme.primary,
            disabledTextColor = colorScheme.primary,
            focusedContainerColor = colorScheme.primaryContainer,
            unfocusedContainerColor = colorScheme.primaryContainer,
            disabledContainerColor = colorScheme.primaryContainer,
            cursorColor = colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTrailingIconColor = colorScheme.primary,
            unfocusedTrailingIconColor = colorScheme.primary,
            disabledTrailingIconColor = colorScheme.primary,
            focusedLeadingIconColor = colorScheme.primary,
            unfocusedLeadingIconColor = colorScheme.primary,
            disabledLeadingIconColor = colorScheme.primary,
            focusedLabelColor = colorScheme.primary.copy(.6f),
            unfocusedLabelColor = colorScheme.primary.copy(.6f),
            disabledLabelColor = colorScheme.primary.copy(.6f),
        )
    }

val textFieldDisableColors
    @Composable get() = run {
        TextFieldDefaults.colors(
            focusedTextColor = colorScheme.primary,
            unfocusedTextColor = colorScheme.primary,
            disabledTextColor = colorScheme.primary.copy(.5f),
            focusedContainerColor = colorScheme.primaryContainer,
            unfocusedContainerColor = colorScheme.primaryContainer,
            disabledContainerColor = colorScheme.primaryContainer.copy(.5f),
            cursorColor = colorScheme.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTrailingIconColor = colorScheme.primary,
            unfocusedTrailingIconColor = colorScheme.primary,
            disabledTrailingIconColor = colorScheme.primary.copy(.5f),
            focusedLabelColor = colorScheme.primary.copy(.6f),
            unfocusedLabelColor = colorScheme.primary.copy(.6f),
            disabledLabelColor = colorScheme.primary.copy(.4f),
        )
    }

val alertDialogColors
    @Composable get() = run {
        AlertDialogColors.colors(
            containerColor = colorScheme.primaryContainer,
            iconContentColor = colorScheme.onPrimaryContainer,
            titleContentColor = colorScheme.onPrimaryContainer,
            textContentColor = colorScheme.onPrimaryContainer,
        )
    }

val alertDialogErrorColors
    @Composable get() = run {
        AlertDialogColors.colors(
            containerColor = colorScheme.errorContainer,
            iconContentColor = colorScheme.onErrorContainer,
            titleContentColor = colorScheme.onErrorContainer,
            textContentColor = colorScheme.onErrorContainer,
        )
    }

val filledIconButtonColors
    @Composable get() = run {
        IconButtonDefaults.filledIconButtonColors(
            containerColor = colorScheme.onPrimary,
            contentColor = colorScheme.primary
        )
    }

val filledIconButtonColorsContrast
    @Composable get() = run {
        IconButtonDefaults.filledIconButtonColors(
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.primary
        )
    }


val switchColors
    @Composable get() = run {
        SwitchDefaults.colors(
            uncheckedThumbColor = colorScheme.onPrimaryContainer.copy(.4f),
            uncheckedTrackColor = colorScheme.onPrimary,
            uncheckedBorderColor = colorScheme.onPrimaryContainer.copy(.4f),
            uncheckedIconColor = colorScheme.onPrimaryContainer.copy(.4f),
        )
    }

val filledIconToggleButtonColors
    @Composable get() = run {
        IconButtonDefaults.filledIconToggleButtonColors(
            containerColor = colorScheme.onPrimary,
            contentColor = colorScheme.onPrimaryContainer,
            checkedContainerColor = colorScheme.onPrimaryContainer,
            checkedContentColor = colorScheme.onPrimary,
            disabledContainerColor = colorScheme.secondaryContainer.copy(.5f),
            disabledContentColor = colorScheme.onSecondaryContainer.copy(.5f),
        )
    }
