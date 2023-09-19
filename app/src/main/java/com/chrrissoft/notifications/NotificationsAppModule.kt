package com.chrrissoft.notifications

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationsAppModule {
    @Provides
    fun provide(@ApplicationContext ctx: Context): NotificationsApp {
        return ctx as NotificationsApp
    }
}
