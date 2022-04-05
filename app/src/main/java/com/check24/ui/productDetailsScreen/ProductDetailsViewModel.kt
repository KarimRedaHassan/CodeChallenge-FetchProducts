package com.check24.ui.productDetailsScreen

import androidx.lifecycle.ViewModel
import com.check24.data.repository.ProductRepository
import com.check24.ui.productsListScreen.DataLoadingStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    /**
     * MutableStateFlow for internal usage ONLY inside the ViewModel
     *
     * All UiState Changes will be sent Up to the View & Events will be sent down to the ViewModel
     * (To Satisfy : Unidirectional Data Flow Design Pattern)
     */
    private val _uiState = MutableStateFlow(

        ProductDetailsScreenUiState(
            product = null, loading = DataLoadingStates.Loading_In_Progress
        )
    )

    /**
     * Immutable StateFlow to be exposed to the View
     * This will restrict the changes to The ViewModel ONLY
     */
    val uiState: StateFlow<ProductDetailsScreenUiState>
        get() = _uiState.asStateFlow()


    /**
     * Update Product ID
     * Check If the product_id is new, perform the request
     * (This will be performed only at the first startup of the viewmodel as the product_id startup value is null)
     * Check if the product_id is the same, Ignore it
     */
    fun updateProductId(product_id: Int) {
        println("${this::class.java.simpleName} -> updateProductId is called -> TEST")
        if ((_uiState.value.product_id != product_id) && (product_id != 0)) {

            _uiState.update {
                it.copy(
                    product_id = product_id
                )
            }.also {
                getProductDetails()
            }
        }

    }

    /**
     * Purpose: Retry Failed Data Loading Request
     * Check if the data loading is failed or not
     * If yes, perform the request
     * If No, Ignore the request
     */
    fun retryFailedDataLoading() {
        println("${this::class.java.simpleName} -> retryFailedDataLoading is called -> TEST ")
        _uiState.value.loading.takeIf { it == DataLoadingStates.Failed }?.let {
            getProductDetails()
        }
    }

    private fun getProductDetails() {

    }

    /**
     * Purpose: Add Or Remove Product from favorites
     */
    fun onFavoriteButtonClicked() {
        println("${this::class.java.simpleName} -> onFavoriteButtonClicked is called -> TEST ")
        val isFavorite = _uiState.value.product?.isFavorite ?: false
        println("${this::class.java.simpleName} -> isFavorite = ${isFavorite} -> TEST ")

        val updatedProduct = _uiState.value.product?.copy()
        updatedProduct?.isFavorite = !isFavorite

        println("${this::class.java.simpleName} -> updatedProduct?.isFavorite = ${updatedProduct?.isFavorite ?: false} -> TEST ")

        _uiState.update {
            it.copy(
                product = updatedProduct,
                forceUpdate = it.forceUpdate + 1
            )
        }
    }


}
