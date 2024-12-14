package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): List<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun doSearch(expression: String, consumer: TracksConsumer)
    fun getResponseState(): Int
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }

}