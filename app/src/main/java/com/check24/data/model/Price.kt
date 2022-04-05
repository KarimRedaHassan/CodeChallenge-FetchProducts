package com.check24.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Price(
    @field:SerializedName("value") @Expose var value: Double? = 0.0,
    @field:SerializedName("currency") @Expose var currency: String? = ""
) {}