package com.check24.data.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Product(
    @field:SerializedName("id") @Expose @PrimaryKey var id: Int = 0,
    @field:SerializedName("name") @Expose @ColumnInfo(name = "name") var name: String? = "",
    @field:SerializedName("description") @Expose @ColumnInfo(name = "description") var description: String? = "",
    @field:SerializedName("longDescription") @Expose @ColumnInfo(name = "longDescription") var longDescription: String? = "",
    @field:SerializedName("imageURL") @Expose @ColumnInfo(name = "imageURL") var imageURL: String? = "",
    @field:SerializedName("type") @Expose @ColumnInfo(name = "type") var type: String? = "",
    @field:SerializedName("color") @Expose @ColumnInfo(name = "color") var color: String? = "",
    @field:SerializedName("colorCode") @Expose @ColumnInfo(name = "colorCode") var colorCode: String? = "",
    @field:SerializedName("available") @Expose @ColumnInfo(name = "available") var available: Boolean? = false,
    @field:SerializedName("releaseDate") @Expose @ColumnInfo(name = "releaseDate") var releaseDate: Int? = 0,
    @field:SerializedName("rating") @Expose @ColumnInfo(name = "rating") var rating: Double? = 0.0,
) {
    @field:SerializedName("price")
    @Expose
    @ColumnInfo(name = "price")
    var price: Price? = null
}
