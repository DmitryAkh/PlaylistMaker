package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.ResponseState
import com.example.playlistmaker.search.domain.Track

import com.example.playlistmaker.creator.Creator


class SearchViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private var latestSearchText: String? = null
    private val tracks: MutableList<Track> = mutableListOf()
    private var historyList: List<Track> = mutableListOf()
    val handler = Handler(Looper.getMainLooper())
    private val interactor = Creator.provideSearchInteractor(getApplication())
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

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}

