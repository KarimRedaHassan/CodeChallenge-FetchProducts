package com.check24.ui.productsListScreen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.check24.data.model.Header
import com.check24.data.model.Product
import com.check24.ui.common.CustomSnackbarHost
import com.check24.ui.common.ImageFromURL
import com.check24.ui.common.RatingBar
import com.check24.ui.productsListScreen.ProductsListScreenConstants.Animation.Placeholder_Animation.Placeholder_Animation_Duration_Milliseconds
import com.check24.ui.productsListScreen.ProductsListScreenConstants.Animation.Placeholder_Animation.Placeholder_Count
import com.check24.ui.productsListScreen.ProductsListScreenConstants.Animation.Placeholder_Animation.Placeholder_Keyframe_For_Middle_Value
import com.check24.ui.productsListScreen.ProductsListScreenConstants.FilterTab.InactiveTabOpacity
import com.check24.ui.productsListScreen.ProductsListScreenConstants.FilterTab.TabFadeInAnimationDuration
import com.check24.ui.productsListScreen.ProductsListScreenConstants.FilterTab.TabFadeOutAnimationDuration
import com.check24.ui.productsListScreen.ProductsListScreenConstants.Products.ProductImageAspectRatio
import com.check24.ui.productsListScreen.ProductsListScreenConstants.Products.ProductImageWidth
import com.check24.ui.productsListScreen.ProductsListScreenConstants.TestTag.Available_Product_Row_Card
import com.check24.ui.productsListScreen.ProductsListScreenConstants.TestTag.Header_Description
import com.check24.ui.productsListScreen.ProductsListScreenConstants.TestTag.Header_Title
import com.check24.ui.productsListScreen.ProductsListScreenConstants.TestTag.Product_Price
import com.check24.ui.productsListScreen.ProductsListScreenConstants.TestTag.Product_Row_Card_Placeholder
import com.check24.ui.productsListScreen.ProductsListScreenConstants.TestTag.Products_Lazy_Vertical_Grid

/**
 * Stateful/Bridge Composable
 *
 * Observe state changes & Make Screen-Specific integrations
 * (To Satisfy : Composable State Hoisting - Best Practices )
 */
@Composable
fun ProductsListScreen(
    productDetailsOnClick: (Product?) -> Unit,
    onFooterClicked: () -> Unit,
    viewModel: ProductsListViewModel = hiltViewModel()
) {

    /**
     * Observe UiState Changes from the ViewModel
     *
     * All UiState Changes will be sent Up to the View & Events will be sent down to the ViewModel
     * (To Satisfy : Unidirectional Data Flow Design Pattern)
     */
    val uiState by viewModel.uiState.collectAsState()
    println("ProductsListScreen -> uiState = ${uiState} -> Test")

    // Remember LazyListState
    val lazyListState = rememberLazyGridState()

    // Remember SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

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


    /**
     * Stateless Composable
     *
     * All States is Hoisted to the Stateful Composable to make testing easier & Achieve more reusable composable
     * (To Satisfy : Composable State Hoisting - Best Practices )
     */
    ProductsListScreen(
        products = uiState.products,
        productDetailsOnClick = productDetailsOnClick,
        lazyListState = lazyListState,
        snackbarHostState = snackbarHostState,
        retryButtonOnClick = { viewModel.retryFailedDataLoading() },
        loading = uiState.loading,
        header = uiState.header,
        filtersList = uiState.filters,
        onFilterSelected = { viewModel.updateFilterSelection(it) },
        currentSelection = uiState.currentFilterSelection,
        onFooterClicked = onFooterClicked
    )
}


/**
 * Stateless Composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListScreen(
    header: Header?,
    filtersList: List<String>?,
    onFilterSelected: (String) -> Unit,
    currentSelection: String,
    onFooterClicked: () -> Unit,
    products: List<Product>?,
    productDetailsOnClick: (Product?) -> Unit,
    retryButtonOnClick: () -> Unit,
    lazyListState: LazyGridState,
    snackbarHostState: SnackbarHostState,
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
        }) { innerPadding ->

        println("ProductsListScreen -> Recomposition After ProductsListScreenUiState Changes -> Test")

        ProductScreenContent(
            products = products,
            innerPadding = innerPadding,
            productDetailsOnClick = productDetailsOnClick,
            lazyListState = lazyListState,
            header = header,
            filtersList = filtersList,
            onFilterSelected = onFilterSelected,
            currentSelection = currentSelection,
            onFooterClicked = onFooterClicked
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProductScreenContent(
    header: Header?,
    filtersList: List<String>?,
    onFilterSelected: (String) -> Unit,
    currentSelection: String,
    onFooterClicked: () -> Unit,
    products: List<Product>?,
    productDetailsOnClick: (Product?) -> Unit,
    innerPadding: PaddingValues,
    lazyListState: LazyGridState
) {
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {

        /**
         * Add Filters Tabs when filters list in Product Response is not Null Nor Empty
         */
        filtersList.takeIf { !it.isNullOrEmpty() }?.let {
            FilterTabs(
                filtersList = it,
                onFilterSelected = onFilterSelected,
                currentSelection = currentSelection
            )
        }


        /**
         * Add Product List
         */
        LazyVerticalGrid(
            modifier = Modifier
                .padding(innerPadding)
                .testTag(Products_Lazy_Vertical_Grid),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            state = lazyListState,
            columns = GridCells.Fixed(1)
        ) {

            /**
             * Add Header when header in Product Response is not Null Nor Empty
             */
            items(1) { index ->
                header.takeIf { it != null }?.let {
                    ListHeader(
                        header = it
                    )
                }
            }

            /**
             * Apply Shimmer Animation using infiniteRepeatable for Alpha value
             */
            products?.count().takeIf { it == 0 }?.let {
                items(Placeholder_Count) { index ->
                    ProductRowCardPlaceholder()
                }
            }

            items(products?.count() ?: 0) { index ->

                val product = products?.get(index)
                val available = (products?.get(index))?.available ?: false

                when (available) {
                    true -> {
                        AvailableProductRowCard(
                            product = product, productDetailsOnClick = productDetailsOnClick
                        )
                    }
                    false -> {
                        NotAvailableProductRowCard(
                            product = product, productDetailsOnClick = productDetailsOnClick
                        )
                    }
                }
            }


            /**
             * Add Footer at the end of the list
             */
            products?.count().takeIf { it != 0 }?.let {
                items(1) { index ->
                    ListFooter(
                        title = "© 2016 Check24", onClick = onFooterClicked
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableProductRowCard(
    product: Product?,
    productDetailsOnClick: (Product?) -> Unit,
) {

    Card(
        modifier = Modifier.testTag(Available_Product_Row_Card)
    ) {

    }
    Column(modifier = Modifier
        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
        .clickable { productDetailsOnClick(product) }
        .shadow(3.dp, shape = RoundedCornerShape(12.0.dp)) /* This is the Default Card Shape */) {

        RowMainDetails(
            product = product
        )
    }
}

@Composable
fun RowMainDetails(
    product: Product?,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(0.dp)
                .height(((ProductImageWidth * ProductImageAspectRatio) + 20).dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            ImageFromURL(
                url = product?.imageURL ?: "",
                modifier = Modifier
                    .width(ProductImageWidth.dp)
                    .height((ProductImageWidth * ProductImageAspectRatio).dp)
                    .padding(start = 10.dp, bottom = 10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.padding(horizontal = 5.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Spacer(Modifier.padding(vertical = 5.dp))
                Text(
                    text = product?.name ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(Header_Title)
                )
                Spacer(Modifier.padding(vertical = 5.dp))
                Text(
                    text = product?.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 3,
                    modifier = Modifier.testTag(Header_Description)
                )
                Spacer(Modifier.padding(vertical = 5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, end = 10.dp)
                        .height(((ProductImageWidth * ProductImageAspectRatio) + 20).dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Price",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(0.dp)
                    )
                    Spacer(Modifier.padding(horizontal = 3.dp))
                    Text(
                        text = product?.price?.value.toString() ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .testTag(Product_Price)
                            .padding(0.dp)
                    )
                    Text(
                        text = product?.price?.currency ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .testTag(Product_Price)
                            .padding(0.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    RatingBar(
                        product?.roundedRating?.toFloat() ?: 0f, modifier = Modifier.height(20.dp)
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotAvailableProductRowCard(
    product: Product?,
    productDetailsOnClick: (Product?) -> Unit,
) {

    Card(
        modifier = Modifier.testTag(Available_Product_Row_Card)
    ) {

    }
    Column(modifier = Modifier
        .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
        .clickable { productDetailsOnClick(product) }
        .shadow(3.dp, shape = RoundedCornerShape(12.0.dp)) /* This is the Default Card Shape */) {

        NotAvailableProductRowMainDetails(
            product = product
        )
    }
}

@Composable
fun NotAvailableProductRowMainDetails(
    product: Product?,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {

        val configuration = LocalConfiguration.current
        val availableScreenWidth =
            configuration.screenWidthDp.dp - 20.dp // the start & end padding for the Card


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(0.dp)
                .height(((ProductImageWidth * ProductImageAspectRatio) + 20).dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Spacer(Modifier.padding(horizontal = 5.dp))

            Column(
                modifier = Modifier
                    .padding(0.dp)
                    .width(availableScreenWidth - ProductImageWidth.dp - 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Spacer(Modifier.padding(vertical = 5.dp))
                Text(
                    text = product?.name ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.testTag(Header_Title)
                )
                Spacer(Modifier.padding(vertical = 5.dp))
                Text(
                    text = product?.description ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 3,
                    modifier = Modifier.testTag(Header_Description)
                )
                Spacer(Modifier.padding(vertical = 5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, end = 10.dp)
                        .height(((ProductImageWidth * ProductImageAspectRatio) + 20).dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Price",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(0.dp)
                    )
                    Spacer(Modifier.padding(horizontal = 3.dp))
                    Text(
                        text = product?.price?.value.toString() ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .testTag(Product_Price)
                            .padding(0.dp)
                    )
                    Text(
                        text = product?.price?.currency ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .testTag(Product_Price)
                            .padding(0.dp)
                    )
                    Spacer(Modifier.weight(1f))
                    RatingBar(
                        product?.roundedRating?.toFloat() ?: 0f, modifier = Modifier.height(20.dp)
                    )
                }
            }

            Spacer(Modifier.padding(horizontal = 5.dp))

            ImageFromURL(
                url = product?.imageURL ?: "",
                modifier = Modifier
                    .width(ProductImageWidth.dp)
                    .height((ProductImageWidth * ProductImageAspectRatio).dp)
                    .padding(end = 10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .border(2.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductRowCardPlaceholder() {

    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = Placeholder_Animation_Duration_Milliseconds
                Placeholder_Keyframe_For_Middle_Value at (Placeholder_Animation_Duration_Milliseconds / 2)
            }, repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = Modifier
            .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp)
            .shadow(3.dp, shape = RoundedCornerShape(12.0.dp))
            .testTag(Product_Row_Card_Placeholder)
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(0.dp)
                    .height(((ProductImageWidth * ProductImageAspectRatio) + 20).dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Box(
                    modifier = Modifier
                        .width(ProductImageWidth.dp)
                        .height((ProductImageWidth * ProductImageAspectRatio).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .padding(start = 10.dp, bottom = 10.dp)
                        .background(color = Color.LightGray.copy(alpha = alpha))
                )

                Spacer(Modifier.padding(horizontal = 5.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Spacer(Modifier.padding(vertical = 5.dp))

                    Box(
                        modifier = Modifier
                            .height(35.dp)
                            .weight(1f)
                            .background(color = Color.LightGray.copy(alpha = alpha))
                    )
                    Spacer(Modifier.padding(vertical = 5.dp))

                    Box(
                        modifier = Modifier
                            .height(35.dp)
                            .weight(1f)
                            .background(color = Color.LightGray.copy(alpha = alpha))
                    )
                    Spacer(Modifier.padding(vertical = 5.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, end = 10.dp)
                            .height(((ProductImageWidth * ProductImageAspectRatio) + 20).dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Box(
                            modifier = Modifier
                                .height(35.dp)
                                .padding(0.dp)
                                .weight(1f)
                                .background(color = Color.LightGray.copy(alpha = alpha))
                        )
                        Spacer(Modifier.padding(horizontal = 3.dp))
                    }
                }

            }
        }

    }
}

@Composable
fun FilterTabs(
    filtersList: List<String>, onFilterSelected: (String) -> Unit, currentSelection: String
) {
    Surface(
        Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Row(Modifier.selectableGroup()) {
            filtersList.forEach { filter ->
                FilterTab(
                    text = filter,
                    onSelected = { onFilterSelected(filter) },
                    selected = currentSelection == filter
                )
            }
        }
    }
}

@Composable
private fun FilterTab(
    text: String, onSelected: () -> Unit, selected: Boolean
) {
    val color = MaterialTheme.colorScheme.onSurface
    val durationMillis = if (selected) TabFadeInAnimationDuration else TabFadeOutAnimationDuration
    val animSpec = remember {
        tween<Color>(
            durationMillis = durationMillis,
            easing = LinearEasing,
            delayMillis = ProductsListScreenConstants.FilterTab.TabFadeInAnimationDelay
        )
    }
    val tabTintColor by animateColorAsState(
        targetValue = if (selected) color else color.copy(alpha = InactiveTabOpacity),
        animationSpec = animSpec
    )
    Row(
        modifier = Modifier
            .padding(16.dp)
            .animateContentSize()
            .height(56.dp)
            .selectable(
                selected = selected, onClick = onSelected
            )
    ) {
        Text(text, color = tabTintColor)
    }
}


@Composable
fun ListHeader(
    header: Header
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {

        Text(
            text = header.headerTitle ?: "",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .testTag(Header_Title)
                .padding(bottom = 5.dp)
        )
        Spacer(Modifier.padding(vertical = 5.dp))
        Text(
            text = header.headerDescription ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.testTag(Header_Description)
        )
    }
}

@Composable
fun ListFooter(
    title: String, onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .testTag(Header_Description)
                .padding(10.dp)
                .clickable { onClick() })
    }
}


@Preview
@Composable
fun ProductsListScreenPreview() {
    ProductsListScreen(products = listOf(),
        productDetailsOnClick = { },
        lazyListState = LazyGridState(),
        snackbarHostState = SnackbarHostState(),
        retryButtonOnClick = { },
        loading = DataLoadingStates.Loading_In_Progress,
        header = Header(),
        filtersList = listOf(),
        onFilterSelected = { },
        currentSelection = "",
        onFooterClicked = { })
}