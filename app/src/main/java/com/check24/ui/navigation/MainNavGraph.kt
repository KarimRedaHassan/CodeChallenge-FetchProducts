package com.check24.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.check24.data.model.Product
import com.check24.ui.productDetailsScreen.ProductDetailsScreen
import com.check24.ui.productsListScreen.ProductsListScreen
import com.check24.ui.webViewScreen.WebViewScreen
import com.check24.utils.Constants
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


/**
 * MainNavGraph
 *
 * The main navigation graph for the application
 */
@Composable
fun MainNavGraph(
    modifier: Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.getMainScreen().route
) {

    /**
     * Get instance of Navigation Action Class
     */
    val actions = remember(navController) { NavigationActions(navController) }

    /**
     * Create NavHost
     */
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(Screen.ProductsListScreen.route) {
            ProductsListScreen(productDetailsOnClick = actions.navigateToProductDetailsScreen,
                onFooterClicked = { actions.navigateToWebViewScreen(Constants.Footer.footerUrl) })
        }

        composable(
            "${Screen.ProductDetailsScreen.route}/{${ArgumentsNameHelper.ProductDetailsScreen.Product_Id}}/{${ArgumentsNameHelper.ProductDetailsScreen.Product}}"
        ) { backStackEntry ->

            val argumentPassed =
                backStackEntry.arguments?.getString(ArgumentsNameHelper.ProductDetailsScreen.Product)
            val product_json = URLDecoder.decode(argumentPassed, StandardCharsets.UTF_8.name())
            val product = product_json?.takeIf { it.isNotEmpty() }
                .let { Gson().fromJson(it, Product::class.java) }

            val product_id =
                backStackEntry.arguments?.getString(ArgumentsNameHelper.ProductDetailsScreen.Product_Id)

            val product_id_INT = product_id?.toInt() ?: 0

            ProductDetailsScreen(
                product_id = product_id_INT, product = product
            )

        }

        composable(
            "${Screen.WebViewScreen.route}/{${ArgumentsNameHelper.WebViewScreen.url}}"
        ) { backStackEntry ->

            val url = backStackEntry.arguments?.getString(ArgumentsNameHelper.WebViewScreen.url)
            val url_decoded = URLDecoder.decode(url, StandardCharsets.UTF_8.name())

            WebViewScreen(
                url = url_decoded
            )
        }
    }
}