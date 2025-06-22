package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.Utils

object DbConverter {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId!!,
            trackName = track.trackName!!,
            artistName = track.artistName!!,
            trackTimeMillis = Utils.secondsToMillis(track.trackTime!!),
            artworkUrl100 = track.artworkUrl100!!,
            collectionName = track.collectionName!!,
            releaseDate = track.releaseDate!!,
            primaryGenreName = track.primaryGenreName!!,
            country = track.country!!,
            previewUrl = track.previewUrl!!,
            additionTime = System.currentTimeMillis(),
            isFavorite = track.isFavorite
        )
    }

    fun map(track: TrackEntity): TrackDto {
        return TrackDto(
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackId = track.trackId,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            coverPath = playlist.coverPath,
            tracksIds = Utils.jsonFromList(playlist.tracks)
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            coverPath = playlist.coverPath,
            tracks = Utils.listFromJson(playlist.tracksIds),
            tracksCount = playlist.tracksCount,
            additionTime = playlist.additionTime
        )
    }

    fun map(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> map(playlist) }
    }



}