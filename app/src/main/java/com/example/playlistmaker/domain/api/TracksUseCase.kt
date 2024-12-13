package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksUseCase {
    fun doSearch(expression: String, consumer: TracksConsumer)
    fun getResultCode(): Int
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}