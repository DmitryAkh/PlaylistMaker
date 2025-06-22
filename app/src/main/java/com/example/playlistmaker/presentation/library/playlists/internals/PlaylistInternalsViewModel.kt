package com.example.playlistmaker.presentation.library.playlists.internals

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.PlaylistInternalsScreenState
import com.example.playlistmaker.domain.entity.PlaylistInternalsState
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.interactors.PlaylistsInteractor
import com.example.playlistmaker.domain.interactors.SearchInteractor
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


}