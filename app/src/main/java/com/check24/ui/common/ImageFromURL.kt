package com.check24.ui.common

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun ImageFromURL(url: String, modifier: Modifier, contentScale: ContentScale) {
    val imageExtension = url.substringAfterLast(".", "")
    val imageRequest: ImageRequest = when (imageExtension.lowercase()) {
        "png", "jpg", "jpeg" -> ImageRequest.Builder(LocalContext.current).data(url).build()
        "svg" -> ImageRequest.Builder(LocalContext.current).data(url)
            .decoderFactory(SvgDecoder.Factory()).build()
        "gif" -> ImageRequest.Builder(LocalContext.current).data(url)
            .decoderFactory(if (Build.VERSION.SDK_INT >= 28) ImageDecoderDecoder.Factory() else GifDecoder.Factory())
            .build()
        else -> ImageRequest.Builder(LocalContext.current).data(url).build()
    }

    Image(
        painter = rememberAsyncImagePainter(model = imageRequest),
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )

}