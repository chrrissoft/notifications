package com.chrrissoft.notifications.actions

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    @Mutable
    fun provideActionsStateMutable(): MutableStateFlow<ActionsState> {
        return MutableStateFlow(ActionsState())
    }

    @Provides
    @Immutable
    fun provideActionsState(@Mutable flow: MutableStateFlow<ActionsState>): StateFlow<ActionsState> {
        return flow.asStateFlow()
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Mutable

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Immutable
}
