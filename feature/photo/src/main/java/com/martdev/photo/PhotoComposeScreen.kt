package com.martdev.photo

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.martdev.domain.photodata.PhotoData
import com.martdev.ui.reusable.TextCompose
import com.martdev.ui.reusable.shimmer
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

const val PHOTO_LAZY_COLUMN = "photo_lazy_colum"

@Composable
fun PhotoComposeScreen(
    goToDetail: (Long, String) -> Unit = {_, _ -> },
    goBack: () -> Unit= {}
) {

    val viewModel: PhotoViewModel = koinViewModel()

    val pager = viewModel.photoList.collectAsLazyPagingItems()

    BackHandler {
        goBack()
    }
    PhotoScreen(pager, goToDetail = goToDetail)
}

@Suppress("ParamsComparedByRef")
@Composable
internal fun PhotoScreen(
    photos: LazyPagingItems<PhotoData>,
    goToDetail: (Long, String) -> Unit = {_, _ -> },
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag(PHOTO_LAZY_COLUMN),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            count = photos.itemCount,
            key = photos.itemKey {
                it.id
            }
        ) {
            val item = photos[it]
            item?.let { photo ->
                PhotoAndPhotographerCompose(photo) {
                    goToDetail(photo.photoId, photo.original)
                }
            }
        }

        photos.loadState.apply {
            when {
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

@Composable
private fun PhotoAndPhotographerCompose(
    photoData: PhotoData,
    click: () -> Unit= {},
) {

    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = click),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(photoData.original)
                    .crossfade(true)
                    .listener(
                        onSuccess = {_, _ -> isLoading = false},
                        onError = {_, _ -> isLoading = false}
                    )
                    .build(),
                contentDescription = photoData.photographer,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .shimmer(true)
            )

            TextCompose(
                text = "Photo by ${photoData.photographer}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
val mockPhotos = listOf(
    PhotoData(1, original = "https://images.pexels.com/photos/34611213/pexels-photo-34611213.jpeg", photographer = "Ik"),
    PhotoData(2, original = "https://images.pexels.com/photos/34624580/pexels-photo-34624580.jpeg", photographer = "martdev"),
    PhotoData(3, original = "https://images.pexels.com/photos/34574841/pexels-photo-34574841.jpeg", photographer = "mart"),
    PhotoData(4, original = "https://images.pexels.com/photos/34705726/pexels-photo-34705726.jpeg", photographer = "dev"),
    PhotoData(5, original = "https://images.pexels.com/photos/34651276/pexels-photo-34651276.jpeg", photographer = "md"),
)

@Preview(showBackground = true)
@Composable
internal fun PhotoPreview() {
    val pagingData = PagingData.from(mockPhotos)
    val photoFlow = flowOf(pagingData)
    PhotoScreen(photoFlow.collectAsLazyPagingItems())
}