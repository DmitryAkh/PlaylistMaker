package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): MutableList<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun searchProcessing(expression: String): Flow<List<Track>?>
    fun getResponseState(): ResponseState
    fun putTrackForPlayer(track: Track)
}
