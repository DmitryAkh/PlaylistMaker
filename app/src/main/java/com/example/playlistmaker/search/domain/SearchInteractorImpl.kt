package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchInteractorImpl(private val repository: SearchRepository) :
    SearchInteractor {


    override fun addTrackToHistory(track: Track) = repository.addTrackToHistory(track)

    override fun clearHistoryList() = repository.clearHistoryList()

    override fun loadHistoryList(): MutableList<Track> = repository.loadHistoryList()

    override fun historyListFromJson(json: String?): MutableList<Track> =
        repository.historyListFromJson(json)

    override fun jsonFromHistoryList(historyList: MutableList<Track>): String =
        repository.jsonFromHistoryList(historyList)

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

    override fun getResponseState(): ResponseState = repository.getResponseState()

    override fun putTrackForPlayer(track: Track) = repository.putTrackForPlayer(track)


}