package com.martdev.photo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.martdev.domain.photodata.PhotoData
import com.martdev.ui.reusable.TextCompose
import org.koin.androidx.compose.koinViewModel


@Composable
fun PhotoComposeScreen(
    goBack: () -> Unit
) {

    val viewModel: PhotoViewModel = koinViewModel()

    val pager = viewModel.photoList.collectAsLazyPagingItems()

    BackHandler {
        goBack()
    }
    PhotoScreen(pager)
}

@Suppress("ParamsComparedByRef")
@Composable
internal fun PhotoScreen(
    photos: LazyPagingItems<PhotoData>
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(
            count = photos.itemCount,
            key = photos.itemKey {
                it.id
            }
        ) {
            val item = photos[it]
            item?.let { photo ->
                TextCompose(photo.photographer, fontSize = 24)
            }
        }

        photos.loadState.apply {
            when {
                refresh is LoadState.Loading || append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
                refresh is LoadState.Error -> {
                    val e = refresh as LoadState.Error
                    item {
                        TextCompose("Error: ${e.error.localizedMessage}")
                    }
                }
                append is LoadState.Error -> {
                    val e = append as LoadState.Error
                    item {
                        TextCompose("Error: ${e.error.localizedMessage}")
                    }
                }
            }
        }
    }
}