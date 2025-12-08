package com.martdev.photo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun PhotoDetailCompose(
    postID: Long,
    imageUrl: String,
    goBack: () -> Unit = {}
) {

    BackHandler {
        goBack()
    }
    PhotoDetail(imageUrl)
}

@Composable
internal fun PhotoDetail(
    imageUrl: String
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()
        .background(Color.Black).padding(16.dp)) {

        AsyncImage(model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
            contentDescription = imageUrl,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}