package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.data.Track


class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {


    override fun preparePlayer(track: Track?) {
        repository.preparePlayer(track)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }


    override fun release() {
        repository.release()
    }

    override fun getState(): PlayerState {
        return repository.getState()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        repository.setOnCompletionListener(listener)
    }
}