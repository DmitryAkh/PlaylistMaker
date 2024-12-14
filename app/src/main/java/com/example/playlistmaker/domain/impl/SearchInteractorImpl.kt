package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchRepository
import com.example.playlistmaker.domain.api.SearchInteractor
import com.example.playlistmaker.domain.models.Track

const val HISTORY_LIST_KEY = "key_for_history_list"


class SearchInteractorImpl(private val repository: SearchRepository) :
    SearchInteractor {


    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearHistoryList() {
        repository.clearHistoryList()
    }

    override fun loadHistoryList(): MutableList<Track> {
        return repository.loadHistoryList()
    }

    override fun historyListFromJson(json: String?): MutableList<Track> {
        return repository.historyListFromJson(json)
    }

    override fun jsonFromHistoryList(historyList: MutableList<Track>): String {
        return repository.jsonFromHistoryList(historyList)
    }

    override fun doSearch(expression: String, consumer: SearchInteractor.TracksConsumer) {
        val t = Thread {
            consumer.consume(repository.doSearch(expression))

        }
        t.start()

    }

    override fun getResponseState(): Int {
        return repository.getResponseState()
    }
}