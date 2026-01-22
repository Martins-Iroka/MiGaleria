package com.martdev.video.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.media3.common.Player
import androidx.media3.ui.compose.ContentFrame

val CONTENT_SCALES = listOf(
    "Fit" to ContentScale.Fit,
    "Crop" to ContentScale.Crop,
    "None" to ContentScale.None,
    "Inside" to ContentScale.Inside,
    "FillBounds" to ContentScale.FillBounds,
    "FillHeight" to ContentScale.FillHeight,
    "FillWidth" to ContentScale.FillWidth
)

@Composable
internal fun BoxScope.MediaPlayer(
    player: Player,
    contentScale: ContentScale,
    keetContentOnReset: Boolean,
    showComments: () -> Unit = {}
) {
    var showControls by remember { mutableStateOf(true) }

    ContentFrame(
        player = player,
        modifier = Modifier.noRippleClickable { showControls = !showControls },
        keepContentOnReset = keetContentOnReset,
        contentScale = contentScale
    )

    if (showControls) {
        Controls(player, showComments)
    }
}

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null
) {
    onClick()
}