package com.check24.ui.productsListScreen

import com.check24.data.model.Header
import com.check24.data.model.Product

/**
 * UI state for ProductsListScreenUiState
 *
 * The Single Source Of Truth for ProductsListScreen UI States
 */
data class ProductsListScreenUiState(
    val filters: List<String>? = emptyList(),
    val currentFilterSelection: String = "",
    val header: Header? = null,
    val products: List<Product> = emptyList(),
    val loading: DataLoadingStates = DataLoadingStates.Loading_In_Progress,
) {
    override fun toString(): String {
        return "ProductsListScreenUiState: " + "{ products.count = ${products.count()}," + " loading = ${loading}}"
    }
}

enum class DataLoadingStates(var message: String) {
    Loading_In_Progress(message = "Loading Data"), Failed(message = "Retry"), Loaded(
        message = "Data Loaded Successfully"
    )
}

