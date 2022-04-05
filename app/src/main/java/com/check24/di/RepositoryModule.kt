package com.check24.di

import com.check24.data.repository.ProductRepository
import com.check24.data.repository.imp.ProductRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
*** This Class contains code needed by hilt to inject a Singleton instance for
*** Repositories
 */
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    /*
    Providing Singleton Implementation For Repositories
     */
    @Binds
    @Singleton
    fun provideProductRepository(productRepositoryImp: ProductRepositoryImp): ProductRepository
}