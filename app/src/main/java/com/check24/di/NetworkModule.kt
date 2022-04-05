package com.check24.di

import androidx.multidex.BuildConfig
import com.check24.di.BaseURLModule.provideBaseUrl
import com.check24.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/*
*** This Class contains code needed by hilt to inject a Singleton instance for
*** Network Related Classes
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /*
    Providing Singleton Instance From Network Related Classes
     */
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        /**
         * We provide a HttpLoggingInterceptor ONLY in the DEBUG mode of the application
         * To avoid Debugging in released versions
         */
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .readTimeout(Constants.NetworkConfig.readTimeoutValue, TimeUnit.SECONDS)
            .connectTimeout(Constants.NetworkConfig.connectTimeoutValue, TimeUnit.SECONDS).build()
    } else {
        OkHttpClient.Builder()
            .readTimeout(Constants.NetworkConfig.readTimeoutValue, TimeUnit.SECONDS)
            .connectTimeout(Constants.NetworkConfig.connectTimeoutValue, TimeUnit.SECONDS).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(provideBaseUrl()).client(okHttpClient).build()

}