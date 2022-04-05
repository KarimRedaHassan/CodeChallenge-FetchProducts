package com.check24.data.remote.utils

object ServerURL {

    object BASE_URL {
        const val url = "https://app.check24.de/"
    }

    object Products {
        const val getProducts: String = BASE_URL.url + QueryHelper.RouteNames.products_test
    }
}