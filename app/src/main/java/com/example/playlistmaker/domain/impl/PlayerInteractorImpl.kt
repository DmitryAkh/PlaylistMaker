package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track


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