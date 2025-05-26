package com.example.playlistmaker.domain.repositories

import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.ResponseState
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun doSearch(expression: String): Flow<Resource<List<Track>>>
    suspend fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    suspend fun loadHistoryList(): MutableList<Track>
    fun historyListFromJson(json: String?): List<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun getResponseState(): ResponseState
    fun putTrackForPlayer(track: Track)
    fun jsonFromTrack(track: Track): String
}