package com.check24.di

import com.check24.data.remote.responseWrapper.NetworkResponseHandler
import com.check24.data.remote.responseWrapper.NetworkResponseHandlerImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
*** This Class contains code needed by hilt to inject a Singleton instance for
*** NetworkResponseHandler
 */

@Module
@InstallIn(SingletonComponent::class)
interface NetworkResponseHandlerModule {

    /*
    Providing Singleton Instance From NetworkResponseHandler Classes
     */
    @Binds
    @Singleton
    fun provideNetworkResponseHandler(networkResponseHandler: NetworkResponseHandlerImp): NetworkResponseHandler
}