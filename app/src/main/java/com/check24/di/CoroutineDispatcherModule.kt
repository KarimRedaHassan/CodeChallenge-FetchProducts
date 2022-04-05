package com.check24.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

/*
*** This Class contains code needed by hilt to inject a Singleton instance for
*** CoroutineDispatcher
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatcherModule {

    /*
    Providing Singleton Coroutine Dispatchers
     */
    @Provides
    @Singleton
    @DefaultDispatcher
    fun provideDefaultCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Singleton
    @IoDispatcher
    fun provideIoCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    @MainDispatcher
    fun provideMainCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Main
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

