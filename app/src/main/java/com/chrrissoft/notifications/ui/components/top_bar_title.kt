package com.chrrissoft.notifications.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TopBarTitle(title: String) {
    Text(text = title, fontWeight = FontWeight.Bold)
}
