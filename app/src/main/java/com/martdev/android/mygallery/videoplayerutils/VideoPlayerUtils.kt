package com.martdev.android.mygallery.videoplayerutils

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

data class PlayerState(
    var window: Int = 0,
    var position: Long = 0,
    var whenReady: Boolean = true,
    var source: String
)

class PlayerHolder(
    private val context: Context,
    private val playerView: PlayerView,
    private val playerState: PlayerState
) : AnkoLogger {

    private val player: ExoPlayer

    init {
        //Create the player instance
        player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context), DefaultTrackSelector(),
        DefaultLoadControl())
            .also {
                playerView.player = it //Bind to the view.
                info { "SimpleExoPlayer created" }
            }
    }

    fun start() {
        //Load media
        val uri = Uri.parse(playerState.source)
        player.prepare(buildMediaSource(uri))
        //Start playback when media has buffered enough
        with(playerState) {
            //Start playback when media has buffered enough
            //whenReady is true by default
            player.playWhenReady = whenReady
            player.seekTo(window, position)
        }
    }

    fun stop() {
        with(player) {
            //Save state
            with(playerState) {
                position = currentPosition
                window = currentWindowIndex
                whenReady = playWhenReady
            }
            //Stop the player
            stop()
        }
        info {
            "SimpleExoPlayer is stopped"
        }
    }

    fun release() {
        player.release()
    }

    private fun buildMediaSource(uri: Uri): ExtractorMediaSource =
        ExtractorMediaSource.Factory(
            DefaultDataSourceFactory(context, "videoapp")
        ).createMediaSource(uri)
}