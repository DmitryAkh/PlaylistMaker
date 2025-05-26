package com.example.playlistmaker.presentation.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactors.SearchInteractor
import com.example.playlistmaker.domain.entity.ResponseState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch


class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {
    private var latestSearchText: String? = null
    private val tracksLiveData = MutableLiveData<List<Track>?>()
    private val historyLiveData = MutableLiveData<List<Track>>()
    private val stateLiveData = MutableLiveData<SearchScreenState>()
    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            doSearch(changedText)
        }

    fun observeState(): LiveData<SearchScreenState> = stateLiveData
    fun observeTracks(): MutableLiveData<List<Track>?> = tracksLiveData
    fun observeHistory(): LiveData<List<Track>> = historyLiveData
    private fun renderState(state: SearchScreenState) = stateLiveData.postValue(state)

    fun doSearch(query: String) {
        renderState(SearchScreenState.Loading)

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
            tracksLiveData.postValue(foundTracks)
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

    fun addTrackToHistory(track: Track) {
        viewModelScope.launch {
            interactor.addTrackToHistory(track)
        }
    }

    fun loadHistoryList() {
        viewModelScope.launch {
            val historyList = interactor.loadHistoryList()
            historyLiveData.postValue(historyList)
        }
    }


    fun clearTracks() {

        tracksLiveData.postValue(emptyList())

    }

    fun clearHistoryList() {
        interactor.clearHistoryList()

    }

    fun putTrackForPlayer(track: Track) = interactor.putTrackForPlayer(track)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}

