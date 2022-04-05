package com.check24.ui.productsListScreen

object ProductsListScreenConstants {

    object Products {
        const val ProductImageAspectRatio = 1 /* Height/Width */
        const val ProductImageWidth = 75
    }

    object TestTag {
        const val Snackbar_Host_Card: String = "Snackbar_Host_Card"
        const val Snackbar_Retry_Button: String = "Snackbar_Retry_Button"
        const val Products_Lazy_Vertical_Grid: String = "Products_Lazy_Vertical_Grid"
        const val Available_Product_Row_Card: String = "Available_Product_Row_Card"
        const val Product_Row_Card_Placeholder: String = "Product_Row_Card_Placeholder"
        const val Product_Name: String = "Product_Name"
        const val Header_Title: String = "Header_Title"
        const val Product_Price : String = "Product_Price"
        const val Header_Description: String = "Header_Description"
        const val Product_Description_: String = "Product_Description_"
        const val Product_View_Details_Button: String = "Product_View_Details_Button"
    }

    object Animation {
        object Placeholder_Animation {
            const val Placeholder_Count: Int = 10
            const val Placeholder_Animation_Initial_Value: Float = 0f
            const val Placeholder_Animation_Target_Value: Float = 1f
            const val Placeholder_Animation_Duration_Milliseconds: Int = 500
            const val Placeholder_Keyframe_For_Middle_Value: Float = 0.7f
        }
    }

    object FilterTab {
        const val InactiveTabOpacity = 0.60f
        const val TabFadeInAnimationDuration = 150
        const val TabFadeInAnimationDelay = 100
        const val TabFadeOutAnimationDuration = 100
    }
}