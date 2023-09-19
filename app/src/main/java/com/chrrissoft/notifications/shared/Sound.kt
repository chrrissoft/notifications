package com.chrrissoft.notifications.shared

import android.media.AudioAttributes
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.chrrissoft.notifications.R
import com.chrrissoft.notifications.Util.toUri
import com.chrrissoft.notifications.Util.ui
import com.chrrissoft.notifications.shared.Sound.Uris.dogUri
import com.chrrissoft.notifications.shared.Sound.Uris.retroUri
import com.chrrissoft.notifications.shared.Sound.Uris.sickUri

enum class Sound(
    val icon: ImageVector,
    val notificationUri: Uri,
    val playerUri: Uri,
) {
    DEFAULT(Icons.Rounded.Favorite, "".toUri, playerUri = "".toUri),
    DOG(Icons.Rounded.Favorite, dogUri, playerUri = R.raw.dog.toUri),
    RETRO(Icons.Rounded.Favorite, retroUri, playerUri = R.raw.retro.toUri),
    SICK(Icons.Rounded.Favorite, sickUri, playerUri = R.raw.sick.toUri),
    ;

    val label: String = this.name.ui

    val attributes: AudioAttributes = run {
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
    }
    companion object {
        val list = buildList {
            add(DEFAULT)
            add(DOG)
            add(RETRO)
            add(SICK)
        }
    }

    object Uris {
        val dogUri = "dog".toUri
        val retroUri = "retro".toUri
        val sickUri = "sick".toUri
    }
}
