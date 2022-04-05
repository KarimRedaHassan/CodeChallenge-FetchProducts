package com.check24.domain.imp

import com.check24.data.model.Product
import com.check24.di.DefaultDispatcher
import com.check24.domain.FilterProductsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterProductsUseCaseImp @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : FilterProductsUseCase {
    override suspend fun invoke(filter: String, list: List<Product>): List<Product> =
        filterProducts(filter = filter, list = list)

    private suspend fun filterProducts(filter: String, list: List<Product>): List<Product> =
        withContext(defaultDispatcher) {
            when (filter) {
                "Alle" -> list
                "Verfügbar" -> filterProductsByAvailability(available = true, list = list)
                "Vorgemerkt" -> filterProductsByFavorite(isFavorite = true, list = list)
                else -> list
            }
        }


    private suspend fun filterProductsByAvailability(
        available: Boolean, list: List<Product>
    ): List<Product> = withContext(defaultDispatcher) {
        list.takeIf { it.isNotEmpty() }?.toMutableList()?.filter { it.available == available }
            ?: run { emptyList() }
    }

    private suspend fun filterProductsByFavorite(
        isFavorite: Boolean, list: List<Product>
    ): List<Product> = withContext(defaultDispatcher) {
        list.takeIf { it.isNotEmpty() }?.toMutableList()?.filter { it.isFavorite == isFavorite }
            ?: run { emptyList() }
    }
}