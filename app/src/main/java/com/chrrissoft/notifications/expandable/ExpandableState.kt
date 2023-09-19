package com.chrrissoft.notifications.expandable

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.media.session.MediaSession
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.List
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.app.NotificationCompat.*
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.core.app.Person
import com.chrrissoft.notifications.Util.ui
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.AbstractStyleState
import com.chrrissoft.notifications.expandable.ExpandableState.BuilderState.StyleState
import com.chrrissoft.notifications.shared.SnackbarData


data class ExpandableState(
    val page: Page = Page.BUILDER,
    val builder: BuilderState = BuilderState(),
    val listing: ListingState = ListingState(),
    val snackbarData: SnackbarData = SnackbarData(),
) {
    enum class Page(val icon: ImageVector) {
        BUILDER(Icons.Rounded.Build),
        LISTING(Icons.Rounded.List),
        ;

        val label = name.ui

        companion object {
            val pages = buildList {
                add(BUILDER)
                add(LISTING)
            }
        }
    }

    data class BuilderState(
        val builder: Builder = Builder.Messaging,
        val mediaStyle: MediaStyleState = MediaStyleState(),
        val callStyle: CallStyleState = CallStyleState(),
        val bigPictureStyle: BigPictureStyleState = BigPictureStyleState(),
        val bigTextStyle: BigTextStyleState = BigTextStyleState(),
        val inboxStyle: InboxStyleState = InboxStyleState(),
        val messagingStyle: MessagingStyleState = MessagingStyleState(),
        val decoratedCustomViewStyle: DecoratedCustomViewStyleState = DecoratedCustomViewStyleState(),
    ) {
        enum class Builder(val label: String, val icon: ImageVector) {
            Media(label = "Media", Icons.Rounded.Favorite),
            Call(label = "Call", Icons.Rounded.Favorite),
            BigPicture(label = "BigPicture", Icons.Rounded.Favorite),
            BigText(label = "BigText", Icons.Rounded.Favorite),
            Inbox(label = "Inbox", Icons.Rounded.Favorite),
            Messaging(label = "Messaging", Icons.Rounded.Favorite),
            DecoratedCustomView(label = "DecoratedCustomView", Icons.Rounded.Favorite),
            ;

            fun resolve(styleState: AbstractStyleState<*>): Builder {
                return when (styleState) {
                    is BigPictureStyleState -> BigPicture
                    is BigTextStyleState -> BigText
                    is CallStyleState -> Call
                    is DecoratedCustomViewStyleState -> DecoratedCustomView
                    is InboxStyleState -> Inbox
                    is MediaStyleState -> Media
                    is MessagingStyleState -> Messaging
                }
            }

            companion object {
                val builders = buildList {
                    add(Media)
                    add(Call)
                    add(BigPicture)
                    add(BigText)
                    add(Inbox)
                    add(Messaging)
                    add(DecoratedCustomView)
                }
            }
        }

        sealed interface AbstractStyleState<S> {
            fun build(): S
        }

        sealed interface StyleState<S : Notification.Style> : AbstractStyleState<S>

        sealed interface CompatStyleState<S : Style> : AbstractStyleState<S>

        data class MediaStyleState(
            val actions: Set<Int> = emptySet(),
            val token: MediaSession.Token? = null,
            val remotePlaybackInfoBuilder: RemotePlaybackInfoBuilder = RemotePlaybackInfoBuilder(),
        ) : StyleState<Notification.MediaStyle> {
            override fun build(): Notification.MediaStyle {
                return Notification.MediaStyle()
                    .apply {
                        if (token!=null) setMediaSession(token)
                        if (SDK_INT >= 34) {
                            setRemotePlaybackInfo(
                                remotePlaybackInfoBuilder.deviceName,
                                remotePlaybackInfoBuilder.iconResource,
                                remotePlaybackInfoBuilder.pendingIntent
                            )
                        }
                        val list = actions.toList()
                        setShowActionsInCompactView(list[0], list[1], list[2])

                    }
            }

            data class RemotePlaybackInfoBuilder(
                val deviceName: String = "",
                val iconResource: Int = 0,
                val pendingIntent: PendingIntent? = null,
            )
        }

        data class CallStyleState(val foo: String = "") : CompatStyleState<CallStyle> {
            override fun build(): CallStyle {
                TODO()
            }
        }

        data class BigPictureStyleState(
            val bitmap: Bitmap? = null,
            val largeBitmap: Bitmap? = null,
            val title: String = "",
            val summaryText: String = "",
            val description: String = "",
            val showBigPictureWhenCollapsed: Boolean = false,
        ) : CompatStyleState<BigPictureStyle> {
            override fun build(): BigPictureStyle {
                return BigPictureStyle()
                    .bigPicture(bitmap)
                    .setBigContentTitle(title)
                    .setSummaryText(summaryText)
                    .bigLargeIcon(largeBitmap)
                    .also {
                        if (SDK_INT >= S) {
                            it.showBigPictureWhenCollapsed(showBigPictureWhenCollapsed)
                            it.setContentDescription(description)
                        }
                    }
            }
        }

        data class BigTextStyleState(
            val text: String = "",
            val title: String = "",
            val summaryText: String = "",
        ) : CompatStyleState<BigTextStyle> {
            override fun build(): BigTextStyle {
                return BigTextStyle()
                    .bigText(text)
                    .setBigContentTitle(title)
                    .setSummaryText(summaryText)
            }
        }

        data class InboxStyleState(
            val title: String = "",
            val summaryText: String = "",
            val lines: Set<String> = emptySet(),
        ) : CompatStyleState<InboxStyle> {
            override fun build(): InboxStyle {
                return InboxStyle()
            }
        }

        data class MessagingStyleState(
            val title: String = "",
            val isGroup: Boolean = false,
            val message: MessageBuilder = MessageBuilder(),
            val historyMessage: MessageBuilder = MessageBuilder(),
            val personBuilder: PersonBuilder = PersonBuilder(),
        ) : CompatStyleState<MessagingStyle> {
            override fun build(): MessagingStyle {
                val builder = MessagingStyle(personBuilder.build())
                message.messages.forEach { builder.addMessage(it.build()) }
                historyMessage.messages.forEach { builder.addHistoricMessage(it.build()) }
                builder.conversationTitle = title
                builder.isGroupConversation = isGroup
                return builder
            }

            data class MessageBuilder(
                val text: String = "",
                val timestamp: Long = System.currentTimeMillis(),
                val personBuilder: PersonBuilder = PersonBuilder(),
                val messages: Set<MessageBuilder> = emptySet(),
            ) {
                fun build(): Message {
                    return Message(text, timestamp, personBuilder.build())
                }
            }

            data class PersonBuilder(
                val name: String = "",
                val selection: PersonBuilder? = null,
                val persons: Set<PersonBuilder> = emptySet(),
            ) {
                fun build(): Person {
                    val bundle = Bundle().apply { putString("name", name) }
                    return Person.fromBundle(bundle)
                }
            }
        }

        data class DecoratedCustomViewStyleState(val foo: String = "") :
            CompatStyleState<DecoratedCustomViewStyle> {
            override fun build(): DecoratedCustomViewStyle {
                return DecoratedCustomViewStyle()
            }
        }
    }

    data class ListingState(
        val type: BuilderState.Builder? = null,
        val listing: List<AbstractStyleState<*>> = emptyList(),
    )
}
