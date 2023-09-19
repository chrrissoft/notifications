package com.chrrissoft.notifications.custom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.chrrissoft.notifications.ui.components.TopBarTitle
import com.chrrissoft.notifications.ui.theme.centerAlignedTopAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScreen(
    state: CustomState,
    onEvent: (CustomEvent) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = centerAlignedTopAppBarColors,
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
                    }
                },
                title = { TopBarTitle(title = "Custom Notifications") },
            )
        },
        containerColor = MaterialTheme.colorScheme.onPrimary,
        content = {},
    )
}
