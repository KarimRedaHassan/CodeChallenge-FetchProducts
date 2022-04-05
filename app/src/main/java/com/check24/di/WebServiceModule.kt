package com.check24.di

import com.check24.data.remote.webservice.ProductsWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/*
*** This Class contains code needed by hilt to inject a Singleton instance for
*** WebServices
 */

@Module
@InstallIn(SingletonComponent::class)
class WebServiceModule {

    /*
    Providing Singleton Instance From WebServices
     */
    @Provides
    @Singleton
    fun provideProductsWebService(retrofit: Retrofit) =
        retrofit.create(ProductsWebService::class.java)
}