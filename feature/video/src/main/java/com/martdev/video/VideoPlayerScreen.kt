package com.martdev.video

import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.martdev.video.utils.CONTENT_SCALES
import com.martdev.video.utils.MediaPlayer

@Composable
internal fun VideoPlayerCompose(videoURL: String) {
    val context = LocalContext.current
    var player by remember { mutableStateOf<Player?>(null) }

    if (Build.VERSION.SDK_INT > 23) {
        LifecycleStartEffect(Unit) {
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

    player?.let {
        VidePlayer(player = it)
    }
}

@Composable
private fun VidePlayer(player: Player, modifier: Modifier = Modifier.fillMaxSize()) {
    var currentContentScaleIndex by remember { mutableIntStateOf(0) }
    var keepContentOnReset by remember { mutableStateOf(false) }

    Box(modifier) {
        MediaPlayer(
            player,
            contentScale = CONTENT_SCALES[currentContentScaleIndex].second,
            keepContentOnReset
        )
        ContentScaleButton(
            currentContentScaleIndex,
            Modifier.align(Alignment.TopCenter).padding(top = 48.dp)
        ) {
            currentContentScaleIndex = currentContentScaleIndex.inc() % CONTENT_SCALES.size
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

private fun initializePlayer(context: Context, videoURL: String): Player =
    ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.Builder().setUri(videoURL).build())
        prepare()
    }