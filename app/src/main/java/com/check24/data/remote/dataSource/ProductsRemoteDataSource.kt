package com.check24.data.remote.dataSource

import com.check24.data.remote.response.ProductsResponse
import com.check24.data.remote.responseWrapper.Resource

interface ProductsRemoteDataSource {
    suspend fun getProducts(): Resource<ProductsResponse>
}