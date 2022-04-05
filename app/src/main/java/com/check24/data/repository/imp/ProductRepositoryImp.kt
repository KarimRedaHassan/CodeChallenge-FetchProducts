package com.check24.data.repository.imp

import com.check24.data.remote.dataSource.ProductsRemoteDataSource
import com.check24.data.remote.response.ProductsResponse
import com.check24.data.remote.responseWrapper.Resource
import com.check24.data.repository.ProductRepository
import com.check24.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImp @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val productsRemoteDataSource: ProductsRemoteDataSource
) : ProductRepository {

    /**
     * withContext(ioDispatcher) is used to make this function (Main-thread Safe)
     * Best Practices
     */
    override suspend fun getProducts(): Resource<ProductsResponse> =
        withContext(ioDispatcher) { productsRemoteDataSource.getProducts() }

}