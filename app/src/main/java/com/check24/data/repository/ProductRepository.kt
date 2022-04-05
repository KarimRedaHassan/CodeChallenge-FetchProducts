package com.check24.data.repository

import com.check24.data.remote.response.ProductsResponse
import com.check24.data.remote.responseWrapper.Resource
import javax.inject.Singleton

@Singleton
interface ProductRepository {
    suspend fun getProducts(): Resource<ProductsResponse>
}