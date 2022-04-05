package com.check24.ui.productDetailsScreen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.check24.R
import com.check24.data.model.Product
import com.check24.ui.common.CustomSnackbarHost
import com.check24.ui.productDetailsScreen.ProductDetailsScreenConstants.TestTag.Product_Color
import com.check24.ui.productDetailsScreen.ProductDetailsScreenConstants.TestTag.Product_Description
import com.check24.ui.productDetailsScreen.ProductDetailsScreenConstants.TestTag.Product_Status
import com.check24.ui.productDetailsScreen.ProductDetailsScreenConstants.TestTag.Product_Type
import com.check24.ui.productsListScreen.DataLoadingStates
import com.check24.ui.productsListScreen.NotAvailableProductRowMainDetails
import com.check24.ui.productsListScreen.RowMainDetails

/**
 * Stateful/Bridge Composable
 *
 * Observe state changes & Make Screen-Specific integrations
 * (To Satisfy : Composable State Hoisting - Best Practices )
 */
@Composable
fun ProductDetailsScreen(
    product_id: Int?, product: Product?, viewModel: ProductDetailsViewModel = hiltViewModel()
) {

    /**
     * Observe UiState Changes from the ViewModel
     *
     * All UiState Changes will be sent Up to the View & Events will be sent down to the ViewModel
     * (To Satisfy : Unidirectional Data Flow Design Pattern)
     */
    val uiState by viewModel.uiState.collectAsState()

    // Remember scrollState
    val scrollState = rememberScrollState()

    // Remember SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    /**
     * Send The product_id to the ViewModel
     * This will be emitted with every recomposition,
     * So, ViewModel is responsible for suppress unnecessary calls
     */
    LaunchedEffect(product_id) {
        snapshotFlow { product_id }.collect {
            viewModel.updateProductId(product_id = product_id ?: 0)
        }
    }


    /**
     * Show Snackbar with a Retry Button when the network request fails
     */
    when (uiState.loading) {
        DataLoadingStates.Loading_In_Progress -> {}
        DataLoadingStates.Failed -> LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                uiState.loading.message, duration = SnackbarDuration.Indefinite
            )
        }
        DataLoadingStates.Loaded -> {}
    }


    ProductDetailsScreenStateless(
        product = uiState.product ?: product,
        scrollState = scrollState,
        snackbarHostState = snackbarHostState,
        retryButtonOnClick = { viewModel.retryFailedDataLoading() },
        onFavoriteButtonClicked = { viewModel.onFavoriteButtonClicked() },
        loading = uiState.loading
    )
}

/**
 * Stateless Composable
 *
 * All States is Hoisted to the Stateful Composable to make testing easier & Achieve more reusable composable
 * (To Satisfy : Composable State Hoisting - Best Practices )
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreenStateless(
    product: Product?,
    scrollState: ScrollState,
    snackbarHostState: SnackbarHostState,
    retryButtonOnClick: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
    loading: DataLoadingStates
) {
    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            CustomSnackbarHost(
                snackbarHostState = snackbarHostState,
                loading = loading,
                retryButtonOnClick = retryButtonOnClick
            )
        }) {

        println("ProductDetailsScreen -> Recomposition After ProductDetailsScreenUiState Changes -> Test")

        ProductDetailsContent(
            product = product,
            scrollState = scrollState,
            onFavoriteButtonClicked = onFavoriteButtonClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsContent(
    product: Product?, scrollState: ScrollState, onFavoriteButtonClicked: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .verticalScroll(enabled = true, state = scrollState)
            .shadow(3.dp, shape = RoundedCornerShape(12.0.dp)) /* This is the Default Card Shape */
    ) {

        val available = product?.available ?: false

        when (available) {
            true -> {
                RowMainDetails(
                    product = product
                )
            }
            false -> {
                NotAvailableProductRowMainDetails(
                    product = product
                )
            }
        }

        Button(onClick = onFavoriteButtonClicked) {

            Column(
                modifier = Modifier.padding(0.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (product?.isFavorite
                            ?: false
                    ) stringResource(R.string.un_favorite) else stringResource(
                        R.string.favorite
                    ), modifier = Modifier
                        .padding(10.dp)
                )
            }


        }
        ProductExtraDetailsCard(product = product)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductExtraDetailsCard(
    product: Product?,
) {

    Card(
        modifier = Modifier
            .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            SingleRow(
                title = stringResource(R.string.type),
                test_tag = Product_Type,
                name = product?.type ?: ""
            )
            SingleRow(
                title = stringResource(R.string.color),
                test_tag = Product_Color,
                name = product?.color ?: ""
            )
            SingleRow(title = stringResource(R.string.available),
                test_tag = Product_Status,
                name = product?.available.takeIf { true }?.let { stringResource(R.string.yes) }
                    ?: run { stringResource(R.string.no) })
            SingleRow(
                title = stringResource(R.string.description),
                test_tag = Product_Description,
                name = product?.longDescription ?: ""
            )
        }
    }
}


@Composable
fun SingleRow(title: String, test_tag: String, name: String) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.padding(end = 10.dp),
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            modifier = Modifier.semantics { testTag = test_tag },
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

    }
}