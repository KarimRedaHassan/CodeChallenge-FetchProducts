package com.check24.di

import com.check24.data.remote.utils.ServerURL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

/*
*** This Class contains code needed by hilt to inject a Singleton instance for
*** Network Related Classes
 */
@Module
@InstallIn(SingletonComponent::class)
object BaseURLModule {

    /*
    Providing Singleton Instance From BaseUrl
     */
    @Provides
    fun provideBaseUrl(): HttpUrl = ServerURL.BASE_URL.url.toHttpUrl()
}