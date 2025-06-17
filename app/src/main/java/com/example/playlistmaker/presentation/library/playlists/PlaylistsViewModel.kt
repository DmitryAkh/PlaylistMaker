package com.example.playlistmaker.presentation.library.playlists

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.domain.interactors.PlaylistsInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    private val playlistsLiveData = MutableLiveData<List<Playlist>>()

    fun observePlaylists(): MutableLiveData<List<Playlist>> = playlistsLiveData

    fun saveImageToPrivateStorage(uri: Uri): String {
        return interactor.saveImageToPrivateStorage(uri)
    }

    fun createPlaylist(name: String, description: String, path: String) {

        val playlist = Playlist(
            playlistName = name,
            playlistDescription = description,
            coverPath = path
        )

        viewModelScope.launch {
            interactor
                .insertPlaylist(playlist)
        }
    }

    fun getPlaylists() {
        viewModelScope.launch {
            interactor
                .getPlaylists()
                .collect { playlists ->
                    playlistsLiveData.postValue(playlists)
                }


        }
    }
}