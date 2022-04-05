package com.check24.data.remote.dataSourceImp

import com.check24.data.remote.dataSource.ProductsRemoteDataSource
import com.check24.data.remote.response.ProductsResponse
import com.check24.data.remote.responseWrapper.NetworkResponseHandler
import com.check24.data.remote.responseWrapper.Resource
import com.check24.data.remote.webservice.ProductsWebService
import com.check24.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRemoteDataSourceImp @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val networkResponseHandler: NetworkResponseHandler,
    private val productsWebService: ProductsWebService
) : ProductsRemoteDataSource {

    override suspend fun getProducts(): Resource<ProductsResponse> = withContext(ioDispatcher) {
        networkResponseHandler.getResponse(
            { productsWebService.getProducts() }, "Error Fetching Products Data"
        )
    }
}