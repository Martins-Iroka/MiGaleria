@file:OptIn(ExperimentalMaterial3Api::class)

package com.martdev.video

import android.content.Context
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.martdev.common.convertUTCToLocalDateTime
import com.martdev.domain.ResponseData
import com.martdev.domain.videodata.VideoPostComments
import com.martdev.ui.reusable.TextCompose
import com.martdev.video.utils.CONTENT_SCALES
import com.martdev.video.utils.MediaPlayer
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
internal fun VideoPlayerCompose(
    postId: Long,
    videoURL: String,
    goBack: () -> Unit = {}) {

    val viewmodel: VideoViewModel = koinViewModel()

    val videoPostComments by viewmodel.videoComments.collectAsStateWithLifecycle()
    val sendCommentResponse by viewmodel.createCommentResponse.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var player by remember { mutableStateOf<Player?>(null) }

    if (Build.VERSION.SDK_INT > 23) {
        LifecycleStartEffect(Unit) {
            Timber.e(videoURL)
            player = initializePlayer(context, videoURL)
            onStopOrDispose {
                player?.apply { release() }
                player = null
            }
        }
    } else {
        LifecycleResumeEffect(Unit) {
            player = initializePlayer(context, videoURL)
            onPauseOrDispose {
                player?.apply { release() }
                player = null
            }
        }
    }

    BackHandler {
        goBack()
    }

    LaunchedEffect(Unit) {
        viewmodel.getCommentsByPostId(postId = postId.toString())
    }
    player?.let {
        VidePlayer(player = it, videoPostComments, sendCommentResponse) {comment ->
            viewmodel.postComment(postId.toString(), comment)
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Suppress("ParamsComparedByRef")
@Composable
private fun VidePlayer(
    player: Player,
    videoComments: ResponseData<List<VideoPostComments>> = ResponseData.NoResponse,
    sendCommentResponse: ResponseData<Nothing> = ResponseData.NoResponse,
    sendComment: (String) -> Unit = {}
) {
    var currentContentScaleIndex by remember { mutableIntStateOf(0) }
    var keepContentOnReset by remember { mutableStateOf(false) }
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(true)

    var comment by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MediaPlayer(
            player,
            contentScale = CONTENT_SCALES[currentContentScaleIndex].second,
            keepContentOnReset
        ) {
            showBottomSheet = true
        }
        ContentScaleButton(
            currentContentScaleIndex,
            Modifier.align(Alignment.TopCenter).padding(top = 48.dp)
        ) {
            currentContentScaleIndex = currentContentScaleIndex.inc() % CONTENT_SCALES.size
        }

        if (showBottomSheet) {
            ModalBottomSheet(onDismissRequest = {
                showBottomSheet = false
            }, sheetState = sheetState, contentWindowInsets = {
                WindowInsets.ime
            }) {
                Scaffold(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f).imePadding(),
                    bottomBar = {
                        OutlinedTextField(value = comment, onValueChange = {input ->
                            comment = input
                        }, modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding().imePadding(),
                            trailingIcon = {
                                if (sendCommentResponse is ResponseData.Loading) {
                                    CircularProgressIndicator()
                                } else {
                                    IconButton(onClick = {
                                        sendComment(comment)
                                        comment = ""
                                    }) {
                                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                                    }
                                }
                            })
                    }) { innerPadding ->
                    LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        when (videoComments) {
                            is ResponseData.Error -> item {
                                Box(modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center) {
                                    TextCompose(videoComments.message)
                                }
                            }
                            ResponseData.Loading -> {
                                item {
                                    CircularProgressIndicator()
                                }
                            }
                            ResponseData.NoResponse -> {
                                item {
                                    Box(modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center) {
                                        TextCompose("Nothing to see")
                                    }
                                }
                            }
                            is ResponseData.Success -> {
                                if (videoComments.data.isNullOrEmpty()) {
                                    item {
                                        Box(modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center) {
                                            TextCompose("Nothing to see")
                                        }
                                    }
                                } else {
                                    items(items = videoComments.data.orEmpty(), key = {
                                        it.id
                                    }) {
                                        CommentCompose(it.content, it.username, it.createdAt)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentScaleButton(
    currentContentScaleIndex: Int,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(onClick, modifier) {
        Text("ContentScale is ${CONTENT_SCALES[currentContentScaleIndex].first}")
    }
}

@Composable
private fun CommentCompose(content: String, username: String, createdAt: String) {
    val localTime = remember(createdAt) {
        try {
            convertUTCToLocalDateTime(createdAt)
        } catch (e: Exception) {
            e.printStackTrace()
            createdAt
        }
    }
    Column(Modifier.fillMaxWidth()) {
        TextCompose(
            text = content,
            textColor = Color.Black
        )
        TextCompose(
            text = "By $username at $localTime",
            textColor = Color.DarkGray
        )
    }
}

private fun initializePlayer(context: Context, videoURL: String): Player =
    ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.Builder().setUri(videoURL).build())
        prepare()
    }