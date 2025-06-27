package com.example.playlistmaker.data.models

import com.example.playlistmaker.domain.entity.Track

data class Playlist(
    val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String,
    val coverPath: String,
    val tracks: MutableList<Track> = mutableListOf(),
    val tracksCount: Int = 0,
    val additionTime: Long = System.currentTimeMillis(),
)
