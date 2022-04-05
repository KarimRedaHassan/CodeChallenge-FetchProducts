package com.check24.ui.productsListScreen

import com.check24.data.model.Header
import com.check24.data.model.Product
import com.check24.data.remote.dataSource.ProductsRemoteDataSource
import com.check24.data.remote.response.ProductsResponse
import com.check24.data.remote.responseWrapper.Resource

internal class FakeProductsRemoteDataSourceImp : ProductsRemoteDataSource {
    override suspend fun getProducts(): Resource<ProductsResponse> {
        val ProductsResponse: ProductsResponse = ProductsResponse(
            header = Header(
                headerTitle = "Title", headerDescription = "Description"
            ), filters = listOf("Alle", "Verfügbar", "Vorgemerkt"), products = products
        )
        return Resource.success(ProductsResponse)
    }

    private val products = listOf(
        Product(
            name = "Yellow Triangle",
            imageURL = "https://kredit.check24.de/konto-kredit/ratenkredit/nativeapps/imgs/08.png",
            available = true
        ), Product(
            name = "Blue Circle",
            imageURL = "https://kredit.check24.de/konto-kredit/ratenkredit/nativeapps/imgs/01.png",
            available = false
        ), Product(
            name = "Yellow Triangle",
            imageURL = "https://kredit.check24.de/konto-kredit/ratenkredit/nativeapps/imgs/01.png",
            available = true
        ), Product(
            name = "Green Circle",
            imageURL = "https://kredit.check24.de/konto-kredit/ratenkredit/nativeapps/imgs/01.png",
            available = true
        ), Product(
            name = "Yellow Triangle",
            imageURL = "https://kredit.check24.de/konto-kredit/ratenkredit/nativeapps/imgs/08.png",
            available = true
        )
    )

}