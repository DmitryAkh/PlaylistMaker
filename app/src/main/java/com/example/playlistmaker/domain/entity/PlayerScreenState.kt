package com.example.playlistmaker.domain.entity

import com.example.playlistmaker.data.models.Playlist

data class PlayerScreenState(
    val playerState: PlayerState,
    val time: String,
    val isFavorite: Boolean,
    val playlists: List<Playlist>,
)