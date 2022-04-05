package com.check24.data.remote.webservice

import com.check24.data.remote.response.ProductsResponse
import com.check24.data.remote.utils.ServerURL
import retrofit2.Response
import retrofit2.http.GET

interface ProductsWebService {
    @GET(ServerURL.Products.getProducts)
    suspend fun getProducts(): Response<ProductsResponse>
}