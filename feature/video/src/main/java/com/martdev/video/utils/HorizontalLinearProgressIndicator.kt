package com.martdev.video.utils

import androidx.annotation.IntRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.media3.common.Player
import androidx.media3.ui.compose.state.rememberProgressStateWithTickCount

@Composable
fun HorizontalLinearProgressIndicatorCompose(
    player: Player,
    modifier: Modifier = Modifier,
    @IntRange(from = 0) totalTickCount: Int = 0
) {
    var ticks by remember(totalTickCount) {
        mutableIntStateOf(totalTickCount)
    }
    val progressState = rememberProgressStateWithTickCount(player, ticks)
}

@Composable
private fun HorizontalLinearProgressIndicator(
    modifier: Modifier = Modifier,
    currentPositionProgress: () -> Float,
    bufferedPositionProgress: () -> Float = currentPositionProgress,
    onLayoutWidthChanged: (Int) -> Unit = {},
    playedColor: Color = Color.Red,
    bufferedColor: Color = Color.LightGray,
    unplayedColor: Color = Color.DarkGray,
    scrubberColor: Color = playedColor,
    scrubberShape: Shape = CircleShape,
    rectHeightDp: Dp = 10.dp
) {
    var positionX by remember { mutableFloatStateOf(0f) }
    var scrubberX by remember { mutableFloatStateOf(0f) }
    var bufferX by remember { mutableFloatStateOf(0f) }
    var barSize by remember { mutableStateOf(Size(0f, 0f)) }
    val rectHeightPx = with(LocalDensity.current) { rectHeightDp.roundToPx()}
    val scrubberBoxSizeDp = 2 * rectHeightDp
    val scrubberBoxSizePx = 2 * rectHeightPx
    val canvasTopDownPaddingDp = 5.dp
    val canvasTopDownPaddingPx = with(LocalDensity.current){ canvasTopDownPaddingDp.roundToPx() }
    val canvasLeftRightPaddingDp = scrubberBoxSizeDp / 2 + canvasTopDownPaddingDp
    val canvasLeftRightPaddingPx = scrubberBoxSizePx / 2 + canvasTopDownPaddingPx

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier.background(Color.Gray.copy(alpha = 0.4f)).height(scrubberBoxSizeDp + 2 * canvasTopDownPaddingDp)
    ) {
        Canvas(
            modifier = Modifier.padding(
                start = canvasLeftRightPaddingDp,
                top = canvasTopDownPaddingDp,
                bottom = canvasTopDownPaddingDp,
                end = canvasLeftRightPaddingDp
            )
                .fillMaxWidth()
                .height(rectHeightDp)
                .onSizeChanged{ (w, _) ->
                    onLayoutWidthChanged(w)
                }
        ) {

            positionX = (currentPositionProgress() * size.width).coerceAtLeast(0f)
            scrubberX = positionX
            bufferX = (bufferedPositionProgress() * size.width).coerceAtLeast(0f)

            barSize = Size(size.width, rectHeightDp.toPx())

            drawRect(unplayedColor, size = barSize)
            drawRect(bufferedColor, size = barSize.copy(width = bufferX))
            drawRect(playedColor, size = barSize.copy(width = positionX))
        }

        val absoluteThumbX = canvasLeftRightPaddingPx + scrubberX
        val scrubberTopLeftX = absoluteThumbX - scrubberBoxSizePx / 2
        Box(
            Modifier.offset { IntOffset(x = scrubberTopLeftX.toInt(), y = 0) }
                .size(scrubberBoxSizeDp)
                .background(scrubberColor, scrubberShape)
        )
    }
}