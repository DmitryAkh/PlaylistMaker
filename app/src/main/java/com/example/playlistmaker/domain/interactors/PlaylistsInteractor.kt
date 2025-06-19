package com.example.playlistmaker.domain.interactors

import android.net.Uri
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun saveImageToPrivateStorage(uri: Uri): String
    suspend fun insertPlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addToPlaylist(playlist: Playlist, track: Track)
}