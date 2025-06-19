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

    private val screenState = MutableLiveData<SearchScreenState>()

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            doSearch(changedText)
        }

    fun observeState(): LiveData<SearchScreenState> = screenState


    fun doSearch(query: String) {
        screenState.postValue(
            SearchScreenState(
                searchState = SearchState.LOADING,
                tracks = emptyList(),
                history = emptyList()
            )
        )
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
            screenState.postValue(
                SearchScreenState(
                    searchState = SearchState.CONTENT,
                    tracks = foundTracks,
                    history = emptyList()
                )
            )
        } else if (responseState == ResponseState.NOT_FOUND) {
            screenState.postValue(
                SearchScreenState(
                    searchState = SearchState.NOTFOUND,
                    tracks = emptyList(),
                    history = emptyList()
                )
            )
        } else {
            screenState.postValue(
                SearchScreenState(
                    searchState = SearchState.NOINTERNET,
                    tracks = emptyList(),
                    history = emptyList()
                )
            )
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
            screenState.postValue(
                SearchScreenState(
                    searchState = SearchState.HISTORY,
                    tracks = emptyList(),
                    history = interactor.loadHistoryList()
                )
            )
        }
    }


    fun clearHistoryList() {
        interactor.clearHistoryList()
        screenState.postValue(
            SearchScreenState(
                searchState = SearchState.CONTENT,
                tracks = emptyList(),
                history = emptyList()
            )
        )
    }

    fun putTrackForPlayer(track: Track) = interactor.putTrackForPlayer(track)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
