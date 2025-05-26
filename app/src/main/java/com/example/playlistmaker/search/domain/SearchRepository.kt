package com.example.playlistmaker.search.domain

interface SearchRepository {
    fun doSearch(expression: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): List<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun getResponseState(): ResponseState
    fun putTrackForPlayer(track: Track)
    fun jsonFromTrack(track: Track): String
}