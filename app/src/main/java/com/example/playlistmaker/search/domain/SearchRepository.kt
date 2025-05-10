package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun doSearch(expression: String): Flow<Resource<List<Track>>>
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): MutableList<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun getResponseState(): ResponseState
    fun putTrackForPlayer(track: Track)
    fun jsonFromTrack(track: Track): String
}