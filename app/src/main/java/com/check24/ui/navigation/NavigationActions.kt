package com.check24.ui.navigation

import androidx.navigation.NavHostController
import com.check24.data.model.Product
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/* MainNavGraph - NavigationActions
 *
 * The purpose of this class is to wrap the navigation actions used in the MainNavGraph
 * inside variable functions to facilitate the navigation process
 * also minimize bug due to failure of providing the right arguments with the navigation action
 *
 */
class NavigationActions(navController: NavHostController) {
    val navigateToProductsListScreen: (String) -> Unit = {
        navController.navigate(Screen.ProductsListScreen.route)
    }
    val navigateToProductDetailsScreen: (product: Product?) -> Unit = { product: Product? ->
        /**
         * Pass Product Object with Navigation Argument
         *
         * Converted the Product Object into Json,
         * Then encode it using UTF-8 so that URL values become compatible with navigation component regulation
         * Then we pass it as a String
         *
         * Later:
         * We decode the string back using UTF-8
         * Then construct the right Object from resulted Json
         *
         */

        val Product_JSON = Gson().toJson(product)
        val Product_JSON_Encoded = URLEncoder.encode(Product_JSON, StandardCharsets.UTF_8.name())
        navController.navigate("${Screen.ProductDetailsScreen.route}/${product?.id ?: ""}/${Product_JSON_Encoded}")
    }

    val navigateToWebViewScreen: (url: String) -> Unit = { url:String ->
        /**
         * encode the URL first
         * so that URL values become compatible with navigation component regulation
         * Then we pass it as a String
         *
         * Later:
         * We decode the string back using UTF-8
         */

        val url_Encoded = URLEncoder.encode(url, StandardCharsets.UTF_8.name())
        navController.navigate("${Screen.WebViewScreen.route}/${url_Encoded ?: ""}")
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}


object ArgumentsNameHelper {

    object ProductsListScreen {}

    object ProductDetailsScreen {
        const val Product_Id: String = "product_id"
        const val Product: String = "product"
    }

    object WebViewScreen {
        const val url: String = "url"
    }
}
