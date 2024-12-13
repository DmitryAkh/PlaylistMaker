package com.example.playlistmaker.domain.api


import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    fun doSearch(expression: String): List<Track>
    fun getResultCode(): Int
}
