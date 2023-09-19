package com.chrrissoft.notifications

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationManagerModule {
    @Provides
    fun provide(@ApplicationContext app: Context): NotificationManager {
        return app.getSystemService(NotificationManager::class.java)
    }

    @Provides
    fun provideCompat(@ApplicationContext app: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(app)
    }
}
