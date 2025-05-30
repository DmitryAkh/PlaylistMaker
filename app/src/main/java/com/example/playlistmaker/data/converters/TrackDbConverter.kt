package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.Utils

object TrackDbConverter {

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

}