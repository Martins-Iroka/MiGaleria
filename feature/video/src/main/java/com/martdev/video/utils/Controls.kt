package com.martdev.video.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.ui.compose.material3.buttons.NextButton
import androidx.media3.ui.compose.material3.buttons.PlayPauseButton
import androidx.media3.ui.compose.material3.buttons.PreviousButton
import androidx.media3.ui.compose.material3.buttons.SeekBackButton
import androidx.media3.ui.compose.material3.buttons.SeekForwardButton
import androidx.media3.ui.compose.material3.indicator.PositionAndDurationText

@Composable
private fun RowControls(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    buttons: List<@Composable () -> Unit>
) {
    Row(modifier, horizontalArrangement, verticalAlignment) {
        buttons.forEach { button ->
            button()
        }
    }
}

@Composable
internal fun BoxScope.Controls(
    player: Player,
    showComments: () -> Unit = {}
) {
    val buttonModifier = Modifier
        .size(50.dp)
        .background(Color.Gray.copy(alpha = 0.1f), CircleShape)

    RowControls(
        Modifier
            .fillMaxWidth()
            .align(Alignment.Center),
        buttons = listOf(
            { PreviousButton(player, buttonModifier) },
            { SeekBackButton(player, buttonModifier) },
            { PlayPauseButton(player, buttonModifier) },
            { SeekForwardButton(player, buttonModifier) },
            { NextButton(player, buttonModifier) },
        )
    )

    Column(Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .align(Alignment.BottomCenter)) {
        HorizontalLinearProgressIndicatorCompose(player, Modifier.fillMaxWidth())
        PositionAndDurationText(player, modifier = Modifier.align(Alignment.Start))
        FloatingActionButton(
            modifier = Modifier.align(alignment = Alignment.End),
            shape = CircleShape,
            onClick = showComments,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                Icons.AutoMirrored.Filled.Comment,
                "",
                tint = Color.White
            )
        }
    }
}