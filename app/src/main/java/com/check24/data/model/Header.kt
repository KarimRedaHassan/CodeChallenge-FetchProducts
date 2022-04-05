package com.check24.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Header(
    @field:SerializedName("headerTitle") @Expose var headerTitle: String? = "",
    @field:SerializedName("headerDescription") @Expose var headerDescription: String? = ""
) {}