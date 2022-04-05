package com.check24.data.remote.response

import com.check24.data.model.Header
import com.check24.data.model.Product
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductsResponse(
    @field:SerializedName("products") @Expose var products: List<Product>? = null,
    @field:SerializedName("header") @Expose var header: Header? = null,
    @field:SerializedName("filters") @Expose var filters: List<String>? = null
)