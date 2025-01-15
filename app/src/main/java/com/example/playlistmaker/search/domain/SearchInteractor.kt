package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.Track

interface SearchInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): List<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun doSearch(expression: String, consumer: TracksConsumer)
    fun getResponseState(): ResponseState
    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }

}