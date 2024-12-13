package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.MediaPlayerUseCase
import com.example.playlistmaker.domain.models.Track


class MediaPlayerUseCaseImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerUseCase {


    private var playerState = STATE_DEFAULT


    override fun preparePlayer(track: Track?) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }


    override fun release() {
        mediaPlayer.release()
    }

    override fun getState(): Int {
        return playerState
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            listener()
        }
    }


    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }
}