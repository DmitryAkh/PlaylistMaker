package com.example.playlistmaker.domain.impl

import android.net.Uri
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.interactors.PlaylistsInteractor
import com.example.playlistmaker.domain.repositories.PlaylistsRepository
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {
    override fun saveImageToPrivateStorage(uri: Uri): String {
        return repository.saveImageToPrivateStorage(uri)
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun addToPlaylist(playlist: Playlist, track: Track) {
        repository.addToPlaylist(playlist, track)
    }

    override fun getOnePlaylist(playlistId: Int?): Flow<Playlist> {
        return repository.getOnePlaylist(playlistId)
    }

    override suspend fun deleteFromPlaylist(trackId: String?, playlist: Playlist) {
        val newTracklist = playlist.tracks.filter {
            it.trackId != trackId
        }.toMutableList()
        val newPlaylist = Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            coverPath = playlist.coverPath,
            tracks = newTracklist,
            tracksCount = playlist.tracksCount,
            additionTime = playlist.additionTime
        )
        repository.deleteFromPlaylist(trackId, newPlaylist)
    }

    override suspend fun deletePlaylist(tracksIds: List<Track>, playlistId: Int) {
        val ids: List<String> = tracksIds.map {
            it.trackId.toString()
        }
        repository.deletePlaylist(ids, playlistId)
    }

    override suspend fun updatePlaylistData(
        playlistId: Int,
        name: String,
        description: String,
        coverPath: String,
    ) {
        repository.updatePlaylistData(
            playlistId,
            name,
            description,
            coverPath
        )
    }
}