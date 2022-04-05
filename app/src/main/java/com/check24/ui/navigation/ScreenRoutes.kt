package com.check24.ui.navigation

import com.check24.R


/*
 * MainNavGraph Screens
 *
 * This class contains all the necessary details for all screens in the MainNavGraph
 * route: is the screen destination route
 * title: default screen title
 * Arguments: if exist, show the possible arguments to be passed with the navigation action
 *
 */

enum class Screen(
    val route: String, val title_id: Int
) {

    ProductsListScreen(
        route = "products_list", title_id = R.string.products
    ),
    ProductDetailsScreen(
        route = "product_details", title_id = R.string.product_details
    ),
    WebViewScreen(
    route = "web_view", title_id = R.string.web_view
    );

    companion object {
        /**
         * Return the Screen Object based on the screen route
         */
        fun getScreenByRout(route: String): Screen {
            val actual_route = route.substringBefore("/")
            return when (actual_route) {
                ProductsListScreen.route -> ProductsListScreen
                ProductDetailsScreen.route -> ProductDetailsScreen
                WebViewScreen.route -> WebViewScreen
                else -> ProductsListScreen
            }
        }

        /**
         * Return the Default Screen of the MainNavGraph
         * Normally The Start Destination
         * Also, default navigation destination when route is missing
         *
         */
        fun getMainScreen() = ProductsListScreen

        /**
         * Return true if the screen is the main screen
         *
         */
        fun Screen.isMainDestination() = (this@isMainDestination == getMainScreen())
    }
}