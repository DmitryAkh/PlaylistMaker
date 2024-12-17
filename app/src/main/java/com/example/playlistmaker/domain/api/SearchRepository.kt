package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.ResponseState
import com.example.playlistmaker.domain.models.Track

interface SearchRepository {
    fun doSearch(expression: String): List<Track>
    fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    fun loadHistoryList(): MutableList<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun getResponseState(): ResponseState
}