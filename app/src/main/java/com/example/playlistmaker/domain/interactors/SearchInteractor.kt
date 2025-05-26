package com.example.playlistmaker.domain.interactors

import com.example.playlistmaker.domain.entity.ResponseState
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    suspend fun addTrackToHistory(track: Track)
    fun clearHistoryList()
    suspend fun loadHistoryList(): MutableList<Track>
    fun historyListFromJson(json: String?): MutableList<Track>
    fun jsonFromHistoryList(historyList: MutableList<Track>): String
    fun searchProcessing(expression: String): Flow<List<Track>?>
    fun getResponseState(): ResponseState
    fun putTrackForPlayer(track: Track)
}
