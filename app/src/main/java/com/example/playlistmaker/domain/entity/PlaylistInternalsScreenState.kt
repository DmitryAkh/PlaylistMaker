package com.example.playlistmaker.domain.entity

import com.example.playlistmaker.data.models.Playlist

data class PlaylistInternalsScreenState(
    val playlist: Playlist,
    val tracklist: List<Track>,
    val playlistState: PlaylistInternalsState,
)