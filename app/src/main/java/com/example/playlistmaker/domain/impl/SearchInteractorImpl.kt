package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.ResponseState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.interactors.SearchInteractor
import com.example.playlistmaker.domain.repositories.SearchRepository
import com.example.playlistmaker.util.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchInteractorImpl(private val repository: SearchRepository) :
    SearchInteractor {


    override suspend fun addTrackToHistory(track: Track) =
        repository.addTrackToHistory(track)

    override fun clearHistoryList() =
        repository.clearHistoryList()

    override suspend fun loadHistoryList(): MutableList<Track> =
        repository.loadHistoryList()

    override fun historyListFromJson(json: String?): MutableList<Track> =
        Utils.listFromJson<Track>(json).toMutableList()

    override fun jsonFromHistoryList(historyList: MutableList<Track>): String =
        Utils.jsonFromList(historyList)

    override fun searchProcessing(expression: String): Flow<List<Track>?> {
        return repository.doSearch(expression).map { resource ->
            when (resource) {
                is Resource.Success<List<Track>> -> {
                    resource.data
                }

                is Resource.Error -> {
                    null
                }
            }
        }
    }

    override fun getResponseState(): ResponseState =
        repository.getResponseState()

    override fun putTrackForPlayer(track: Track) =
        repository.putTrackForPlayer(track)


}