package com.check24.ui.productDetailsScreen

import com.check24.data.model.Product
import com.check24.ui.productsListScreen.DataLoadingStates

/**
 * UI state for ProductDetailsScreenUiState
 *
 * The Single Source Of Truth for ProductDetailsScreen UI States
 */
data class ProductDetailsScreenUiState(
    var product: Product? = null,
    val product_id: Int? = null,
    val loading: DataLoadingStates = DataLoadingStates.Loading_In_Progress,
    val forceUpdate: Int = 0
) {
    override fun toString(): String {
        return "ProductDetailsScreenUiState: " + "{ product = ${product}," + " loading = ${loading}," + " product_id = ${product_id}}"
    }
}