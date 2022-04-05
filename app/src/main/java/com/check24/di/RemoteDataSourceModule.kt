package com.check24.di

import com.check24.data.remote.dataSource.ProductsRemoteDataSource
import com.check24.data.remote.dataSourceImp.ProductsRemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/*
*** This Class contains code needed by hilt to inject a Singleton instance for
*** RemoteDataSources
 */

@Module
@InstallIn(SingletonComponent::class)
interface RemoteDataSourceModule {

    /*
    Providing Singleton Implementation For RemoteDataSources
     */
    @Binds
    @Singleton
    fun provideProductsRemoteDataSource(productsRemoteDataSourceImp: ProductsRemoteDataSourceImp): ProductsRemoteDataSource

}