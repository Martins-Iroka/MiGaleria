package com.martdev.video

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.martdev.domain.videodata.VideoData
import com.martdev.ui.reusable.TextCompose
import com.martdev.ui.reusable.shimmer
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

const val VIDEO_LAZY_COLUMN = "video_lazy_colum"

@Composable
fun VideoComposeScreen(
    goBack: () -> Unit = {}
) {
    val viewModel: VideoViewModel = koinViewModel()

    val pager = viewModel.videoList.collectAsLazyPagingItems()

    BackHandler {
        goBack()
    }

    VideoScreen(pager)
}

@Suppress("ParamsComparedByRef")
@Composable
internal fun VideoScreen(
    videos: LazyPagingItems<VideoData>,
    click: (Long) -> Unit = {}
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag(VIDEO_LAZY_COLUMN),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            count = videos.itemCount,
            key = videos.itemKey {
                it.id
            }
        ) {
            val item = videos[it]
            item?.let { video ->
                VideoItemCompose(video) {
                    click(video.id)
                }
            }
        }

        videos.loadState.apply {
            val refreshState = refresh
            val appendState = append
            when {
                refreshState is LoadState.Error -> {
                    item {
                        TextCompose("Error: ${refreshState.error.localizedMessage}")
                    }
                }

                appendState is LoadState.Error -> {
                    item {
                        TextCompose("Error: ${appendState.error.localizedMessage}")
                    }
                }
            }
        }
    }
}

@Suppress("ParamsComparedByRef")
@Composable
private fun VideoItemCompose(
    videoData: VideoData,
    click: () -> Unit = {}
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = click)
            .testTag(videoData.id.toString())
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(videoData.videoImage)
                    .crossfade(true)
                    .listener(
                        onStart = {
                            isLoading = true
                        },
                        onSuccess = {_, _ -> isLoading = false},
                        onError = {_, _ -> isLoading = false}
                    )
                    .build(),
                contentDescription = videoData.videoImage,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .shimmer(isLoading)
            )

            Image(imageVector = Icons.Filled.PlayCircle,
                contentDescription = "play",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp))
        }

    }
}

val mockVideos = (1L..6).map {
    VideoData(
        id = it,
        videoImage = "https://images.pexels.com/videos/6963395/eco-friendly-environment-environmentally-friendly-mothernature-6963395.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=630"
    )
}

@Preview(showBackground = true)
@Composable
private fun VideoPreview() {
    val pagingData = PagingData.from(mockVideos)
    val videoFlow = flowOf(pagingData)
    VideoScreen(videoFlow.collectAsLazyPagingItems())
}