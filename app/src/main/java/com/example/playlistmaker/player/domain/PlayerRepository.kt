package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.data.Track

interface PlayerRepository {
    fun preparePlayer(track: Track?)
    fun startPlayer()
    fun pausePlayer()
    fun release()
    fun getState(): PlayerState
    fun getCurrentPosition(): Int
    fun setOnCompletionListener(listener: () -> Unit)
}