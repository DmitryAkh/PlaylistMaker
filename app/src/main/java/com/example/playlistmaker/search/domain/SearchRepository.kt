package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.Track

interface SearchRepository {
    fun doSearch(expression: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): List<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun getResponseState(): ResponseState
}