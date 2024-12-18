package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.PlayerState.PLAYING
import com.example.playlistmaker.domain.models.PlayerState.PREPARED
import com.example.playlistmaker.domain.models.PlayerState.PAUSED
import com.example.playlistmaker.domain.models.PlayerState.DEFAULT
import com.example.playlistmaker.domain.models.Track

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {
    private var playerState = DEFAULT


    override fun preparePlayer(track: Track?) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PAUSED
    }


    override fun release() {
        mediaPlayer.release()
    }

    override fun getState(): PlayerState {
        return playerState
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            playerState = PREPARED
            listener()
        }
    }
}