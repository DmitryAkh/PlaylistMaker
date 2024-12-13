package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): MutableList<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String

}