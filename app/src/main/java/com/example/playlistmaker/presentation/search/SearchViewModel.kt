package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.ResponseState
import com.example.playlistmaker.search.domain.Track


class SearchViewModel(
    private val interactor: SearchInteractor,
) : ViewModel() {
    private var latestSearchText: String? = null
    private val tracks: MutableList<Track> = mutableListOf()
    private var historyList: List<Track> = mutableListOf()
    private val handler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<SearchScreenState>()

    fun observeState(): LiveData<SearchScreenState> = stateLiveData

    private fun renderState(state: SearchScreenState) = stateLiveData.postValue(state)

    fun doSearch(query: String) {
        renderState(SearchScreenState.Loading)
        tracks.clear()
        val responseState = interactor.getResponseState()

        Thread {
            interactor.doSearch(query, object : SearchInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    Handler(Looper.getMainLooper()).post {

                        if (foundTracks != null) {
                            tracks.addAll(foundTracks)
                            renderState(SearchScreenState.Content)
                        } else if (responseState == ResponseState.NOT_FOUND) {
                            renderState(SearchScreenState.NotFound)
                        } else {
                            renderState(SearchScreenState.NoInternet)
                        }
                    }
                }

            })
        }.start()
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {

        if (changedText == latestSearchText) {
            return
        }

        latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable {
            if (changedText.isNotEmpty())
                doSearch(changedText)
        }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )

    }

    fun addTrackToHistory(track: Track) = interactor.addTrackToHistory(track)

    fun getTracks(): List<Track> = tracks
    private fun loadHistoryList() {
        historyList = interactor.loadHistoryList()

    }

    fun getHistoryList(): List<Track> {
        loadHistoryList()
        return historyList
    }

    fun clearTracks() = tracks.clear()

    fun clearHistoryList() = interactor.clearHistoryList()

    fun putTrackForPlayer(track: Track) = interactor.putTrackForPlayer(track)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()


    }
}

