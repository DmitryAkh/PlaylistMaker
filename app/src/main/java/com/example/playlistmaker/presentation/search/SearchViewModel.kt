package com.example.playlistmaker.presentation.search


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.interactors.SearchInteractor
import com.example.playlistmaker.domain.entity.ResponseState
import com.example.playlistmaker.domain.entity.SearchScreenState
import com.example.playlistmaker.domain.entity.SearchState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.launch


class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {

    private var latestSearchText: String? = null
    private var state: SearchState = SearchState.DEFAULT

    private var tracks: List<Track> = emptyList()
    private var history: List<Track> = emptyList()

    private val screenState = MutableLiveData<SearchScreenState>()

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            doSearch(changedText)
        }

    fun observeState(): LiveData<SearchScreenState> = screenState

    private fun renderState(state: SearchState) {
        this.state = state
        screenState.postValue(
            SearchScreenState(
                searchState = state,
                tracks = tracks,
                history = history
            )
        )
    }

    fun doSearch(query: String) {
        renderState(SearchState.LOADING)

        viewModelScope.launch {
            interactor
                .searchProcessing(query)
                .collect { resultTracks ->
                    processResult(resultTracks)
                }
        }
    }

    private fun processResult(foundTracks: List<Track>?) {
        val responseState = interactor.getResponseState()

        if (foundTracks != null) {
            tracks = foundTracks
            renderState(SearchState.CONTENT)
        } else if (responseState == ResponseState.NOT_FOUND) {
            tracks = emptyList()
            renderState(SearchState.NOTFOUND)
        } else {
            tracks = emptyList()
            renderState(SearchState.NOINTERNET)
        }
    }

    fun searchDebounce(changedText: String) {
        if (changedText == latestSearchText) return
        latestSearchText = changedText

        if (changedText.isNotEmpty()) {
            tracksSearchDebounce(changedText)
        }
    }

    fun addTrackToHistory(track: Track) {
        viewModelScope.launch {
            interactor.addTrackToHistory(track)
        }
    }

    fun loadHistoryList() {
        viewModelScope.launch {
            history = interactor.loadHistoryList()
            renderState(SearchState.HISTORY)
        }
    }

    fun clearTracks() {
        tracks = emptyList()
        renderState(state)
    }

    fun clearHistoryList() {
        interactor.clearHistoryList()
        history = emptyList()
        renderState(state)
    }

    fun putTrackForPlayer(track: Track) = interactor.putTrackForPlayer(track)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
