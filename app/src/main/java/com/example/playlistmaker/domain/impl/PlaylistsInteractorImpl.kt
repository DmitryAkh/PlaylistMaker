package com.example.playlistmaker.domain.impl

import android.net.Uri
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.interactors.PlaylistsInteractor
import com.example.playlistmaker.domain.repositories.PlaylistsRepository

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {
    override fun saveImageToPrivateStorage(uri: Uri): String {
        return repository.saveImageToPrivateStorage(uri)
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

    override suspend fun getPlaylists(): List<Playlist> {
        return repository.getPlaylists()
    }

    override suspend fun addToPlaylist(playlist: Playlist, track: Track) {
        repository.addToPlaylist(playlist, track)
    }
}