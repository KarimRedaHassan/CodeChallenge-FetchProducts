package com.check24.di

import com.check24.data.repository.ProductRepository
import com.check24.data.repository.imp.ProductRepositoryImp
import com.check24.domain.FilterProductsUseCase
import com.check24.domain.imp.FilterProductsUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
*** This Class contains code needed by hilt to inject a Singleton instance for
*** UseCases
 */
@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    /*
    Providing Singleton Implementation For Repositories
     */
    @Binds
    @Singleton
    fun provideFilterProductsUseCase(filterProductsUseCaseImp: FilterProductsUseCaseImp): FilterProductsUseCase
}