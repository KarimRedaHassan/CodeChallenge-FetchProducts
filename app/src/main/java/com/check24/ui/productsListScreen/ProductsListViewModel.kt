package com.check24.ui.productsListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.check24.data.model.Product
import com.check24.data.remote.responseWrapper.Status
import com.check24.data.repository.ProductRepository
import com.check24.domain.FilterProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val filterProductsUseCase: FilterProductsUseCase
) : ViewModel() {

    /**
     * MutableStateFlow for internal usage ONLY inside the ViewModel
     *
     * All UiState Changes will be sent Up to the View & Events will be sent down to the ViewModel
     * (To Satisfy : Unidirectional Data Flow Design Pattern)
     */
    private val _uiState = MutableStateFlow(
        ProductsListScreenUiState(
            products = listOf(), loading = DataLoadingStates.Loading_In_Progress
        )
    )

    /**
     * Immutable StateFlow to be exposed to the View
     * This will restrict the changes to The ViewModel ONLY
     */
    val uiState: StateFlow<ProductsListScreenUiState>
        get() = _uiState.asStateFlow()

    /**
     * To Store the original fetched products from the network
     */
    private var originalProductList: List<Product> = listOf()

    /**
     * Invoke calls once the ViewModel is loaded
     */
    init {
        getInitialProducts()
    }

    /**
     * Get Initial Products Data
     */
    private fun getInitialProducts() {
        println("${this::class.java.simpleName} -> getInitialProducts is called -> DEBUG")
        getProducts()
    }

    /**
     * Purpose: Retry Failed Data Loading Request
     */
    fun retryFailedDataLoading() {
        println("${this::class.java.simpleName} -> retryFailedDataLoading is called -> DEBUG")
        getProducts()
    }

    /**
     * Perform The Actual Data Fetching From The Repository
     * This is ONLY method which access the Repository
     * To Better Control The Data Fetching & States
     */
    private fun getProducts() = viewModelScope.launch {
        println("${this::class.java.simpleName} -> getProducts is called -> -> DEBUG")
        _uiState.update {
            it.copy(
                loading = DataLoadingStates.Loading_In_Progress
            )
        }

        productRepository.getProducts().let { productResource ->
            when (productResource.status) {
                /**
                 * Check if the response is status SUCCESS
                 * Make sure that the data is returned also
                 */
                Status.SUCCESS -> productResource.data?.let { productsResponse ->
                    /**
                     * Make sure that the ProductsResponse has some product results
                     */
                    productsResponse.products?.let { products ->
                        /**
                         * Update the original product list
                         */
                        originalProductList = products

                        /**
                         * Update the UiState MutableStateFlow
                         */
                        _uiState.update {
                            it.copy(
                                products = filterProductsUseCase(
                                    list = products,
                                    filter = productsResponse.filters?.first() ?: ""
                                ),
                                filters = productsResponse.filters,
                                currentFilterSelection = productsResponse.filters?.first() ?: "",
                                header = productsResponse.header,
                                loading = DataLoadingStates.Loaded,
                            )
                        }

                    } ?:
                    /**
                     * if ProductsResponse doesn't have results
                     * This means no more results available on the server
                     */
                    run {
                        println("${this::class.java.simpleName} -> getProducts NO RESULTS -> DEBUG")
                        _uiState.update {
                            it.copy(
                                loading = DataLoadingStates.Failed
                            )
                        }
                    }

                } ?:
                /**
                 * if Data is null,
                 * Propagate an Error to the UI
                 */
                run {
                    println("${this::class.java.simpleName} -> getProducts FALIED -> DEBUG")
                    _uiState.update {
                        it.copy(
                            loading = DataLoadingStates.Failed
                        )
                    }
                }

                /**
                 * Check if the response is status ERROR
                 */
                Status.ERROR -> {
                    /**
                     * Propagate an Error to the UI
                     */
                    println("${this::class.java.simpleName} -> getProducts FALIED -> DEBUG")
                    _uiState.update {
                        it.copy(
                            loading = DataLoadingStates.Failed
                        )
                    }
                }

                /**
                 * Never Returned from Repository (As we don't depend on Flow in the repository)
                 */
                Status.LOADING -> {}
            }
        }
    }


    /**
     * Purpose: updateFilterSelection
     */
    fun updateFilterSelection(currentFilterSelection: String) {
        println("${this::class.java.simpleName} -> updateFilterSelection is called -> DEBUG -> currentFilterSelection = ${currentFilterSelection}")
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    products = filterProductsUseCase(
                        list = originalProductList, filter = currentFilterSelection
                    ),
                    currentFilterSelection = currentFilterSelection,
                )
            }
        }
    }

}
