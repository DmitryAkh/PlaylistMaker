package com.example.playlistmaker.player.domain

import android.media.MediaPlayer
import com.example.playlistmaker.search.domain.Track


class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {


    override fun preparePlayer() = repository.preparePlayer()

    override fun startPlayer() = repository.startPlayer()

    override fun pausePlayer() = repository.pausePlayer()


    override fun release() = repository.release()


    override fun getState(): PlayerState = repository.getState()


    override fun getCurrentPosition(): Int = repository.getCurrentPosition()

    override fun setOnCompletionListener(listener: () -> Unit) {
        repository.setOnCompletionListener(listener)
    }

    override fun getTrack(): Track = repository.getTrack()
    override fun getPlayer(): MediaPlayer = repository.getPlayer()

}