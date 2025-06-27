package com.example.playlistmaker.presentation.library.playlists.internals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.PlaylistInternalsScreenState
import com.example.playlistmaker.domain.entity.PlaylistInternalsState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.interactors.PlaylistsInteractor
import com.example.playlistmaker.domain.interactors.SearchInteractor
import com.example.playlistmaker.util.Utils
import kotlinx.coroutines.launch

open class PlaylistInternalsViewModel(
    val interactor: PlaylistsInteractor,
    private val searchInteractor: SearchInteractor,
) : ViewModel() {
    private val screenState = MutableLiveData<PlaylistInternalsScreenState>()


    fun observeScreenState(): MutableLiveData<PlaylistInternalsScreenState> = screenState
    fun getPlaylist(playlistId: Int?) {
        viewModelScope.launch {
            interactor
                .getOnePlaylist(playlistId)
                .collect { playlist ->
                    if (playlist.tracks.size > 0) {
                        screenState.postValue(
                            PlaylistInternalsScreenState(
                                playlist, playlist.tracks.reversed(),
                                PlaylistInternalsState.CONTENT
                            )
                        )
                    } else {
                        screenState.postValue(
                            PlaylistInternalsScreenState(
                                playlist, playlist.tracks,
                                PlaylistInternalsState.EMPTY
                            )
                        )
                    }
                }
        }
    }

    fun deleteTrackFromPlaylist(trackId: String?) {
        val playlist = screenState.value?.playlist
        viewModelScope.launch {
            if (playlist != null) {
                interactor.deleteFromPlaylist(trackId, playlist)
            }
        }
    }

    fun deletePlaylist(tracksIds: List<Track>, playlistId: Int) {
        viewModelScope.launch {
            interactor.deletePlaylist(tracksIds, playlistId)
        }
    }

    fun putTrackForPlayer(track: Track) = searchInteractor.putTrackForPlayer(track)

    fun calculateMinutes(tracks: List<Track>): Pair<String, Int> {
        val totalMillis = tracks.sumOf {
            it.trackTime?.let { timeStr ->
                val parts = timeStr.split(":")
                val minutes = parts.getOrNull(0)?.toIntOrNull() ?: 0
                val seconds = parts.getOrNull(1)?.toIntOrNull() ?: 0
                (minutes * 60 + seconds) * 1000
            } ?: 0
        }
        return Pair(Utils.millisToMinutes(totalMillis.toLong()), totalMillis)
    }
}