package com.check24.domain

import com.check24.data.model.Product
import javax.inject.Singleton

@Singleton
interface FilterProductsUseCase {
    suspend operator fun invoke(
        filter: String = "", list: List<Product>
    ): List<Product>
}