package com.check24.ui.productsListScreen

import com.check24.data.remote.dataSource.ProductsRemoteDataSource
import com.check24.data.repository.ProductRepository
import com.check24.data.repository.imp.ProductRepositoryImp
import com.check24.domain.FilterProductsUseCase
import com.check24.domain.imp.FilterProductsUseCaseImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ProductsListViewModelTest {
    // Class Under Test
    private lateinit var viewModel: ProductsListViewModel

    // Other Classes
    private lateinit var productRepository: ProductRepository
    private lateinit var productsRemoteDataSource: ProductsRemoteDataSource
    private lateinit var filterProductsUseCase: FilterProductsUseCase

    // Coroutine Test Dispatcher
    val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        productsRemoteDataSource = FakeProductsRemoteDataSourceImp()
        productRepository = ProductRepositoryImp(testDispatcher, productsRemoteDataSource)
        filterProductsUseCase = FilterProductsUseCaseImp(testDispatcher)

        viewModel = ProductsListViewModel(productRepository, filterProductsUseCase).also {
            // We perform runCurrent() once we initialize the ProductsListViewModel
            // To immediately execute the suspend function which exist in the init{} Block of the ProductsListViewModel
            runTest(testDispatcher) { runCurrent() }
        }
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }

    @Test
    fun `ViewModel Initialization Should Get Data From Repository`() = runTest(testDispatcher) {
        // When - ViewModel is created (done by setUp function as it's annotated by @BeforeEach

        //Then
        assertThat(viewModel.uiState.value.products.count(), `is`(greaterThan(1)))
    }
}