package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.Track


class SearchInteractorImpl(private val repository: SearchRepository) :
    SearchInteractor {


    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistoryList() {
        repository.clearHistoryList()
    }

    override fun loadHistoryList(): List<Track> {
        return repository.loadHistoryList()
    }

    override fun historyListFromJson(json: String?): MutableList<Track> {
        return repository.historyListFromJson(json)
    }

    override fun jsonFromHistoryList(historyList: MutableList<Track>): String {
        return repository.jsonFromHistoryList(historyList)
    }

    override fun doSearch(expression: String, consumer: SearchInteractor.TracksConsumer) {
        when (val resource = repository.doSearch(expression)) {
            is Resource.Success -> consumer.consume(resource.data, null)
            is Resource.Error -> consumer.consume(null, resource.message)
        }

    }

    override fun getResponseState(): ResponseState {
        return repository.getResponseState()
    }


}