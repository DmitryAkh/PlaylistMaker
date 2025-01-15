package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.util.Resource

interface SearchRepository {
    fun doSearch(expression: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): MutableList<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun getResponseState(): ResponseState
}