package com.chrrissoft.notifications

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.chrrissoft.notifications.Util.setBarsColors
import com.chrrissoft.notifications.navigation.Graph
import com.chrrissoft.notifications.ui.components.App
import com.chrrissoft.notifications.ui.theme.NotificationsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App {
                setBarsColors()
                Graph()
            }
        }
    }
}
