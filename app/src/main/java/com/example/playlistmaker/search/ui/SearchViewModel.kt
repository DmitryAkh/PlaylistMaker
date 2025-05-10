package com.example.playlistmaker.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.ResponseState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch


class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {
    private var latestSearchText: String? = null
    private var tracksList: MutableList<Track> = mutableListOf()
    private var historyList: MutableList<Track> = mutableListOf()
    private val stateLiveData = MutableLiveData<SearchScreenState>()
    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            doSearch(changedText)
        }

    fun getTracks(): List<Track> = tracksList
    fun observeState(): LiveData<SearchScreenState> = stateLiveData

    private fun renderState(state: SearchScreenState) = stateLiveData.postValue(state)

    fun doSearch(query: String) {
        renderState(SearchScreenState.Loading)
        tracksList.clear()

        viewModelScope.launch {

            interactor
                .searchProcessing(query)
                .collect { tracks ->
                    processResult(tracks)
                }
        }


    }

    private fun processResult(foundTracks: List<Track>?) {
        val responseState = interactor.getResponseState()

        if (foundTracks != null) {
            tracksList.addAll(foundTracks)
            renderState(SearchScreenState.Content)
        } else if (responseState == ResponseState.NOT_FOUND) {
            renderState(SearchScreenState.NotFound)
        } else {
            renderState(SearchScreenState.NoInternet)
        }
    }


    fun searchDebounce(changedText: String) {

        if (changedText == latestSearchText) {
            return
        }

        latestSearchText = changedText

        if (changedText.isNotEmpty())
            tracksSearchDebounce(changedText)


    }

    fun addTrackToHistory(track: Track) = interactor.addTrackToHistory(track)

    private fun loadHistoryList() {
        historyList = interactor.loadHistoryList()

    }

    fun getHistoryList(): List<Track> {
        loadHistoryList()
        return historyList
    }

    fun clearTracks() {

        tracksList.clear()

    }

    fun clearHistoryList() {
        interactor.clearHistoryList()

    }

    fun putTrackForPlayer(track: Track) = interactor.putTrackForPlayer(track)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}

