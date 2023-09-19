package com.chrrissoft.notifications.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.chrrissoft.notifications.ui.components.TopBarTitle
import com.chrrissoft.notifications.ui.theme.centerAlignedTopAppBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonScreen(
    state: CommonState,
    onEvent: (CommonEvent) -> Unit,
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
                title = { TopBarTitle(title = "Common Notifications") },
            )
        },
        containerColor = MaterialTheme.colorScheme.onPrimary,
        content = {},
    )
}
