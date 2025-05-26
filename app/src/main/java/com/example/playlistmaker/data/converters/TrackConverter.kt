package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.util.Utils

object TrackConverter {
    fun map(trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            collectionName = trackDto.collectionName,
            primaryGenreName = trackDto.primaryGenreName,
            artworkUrl100 = trackDto.artworkUrl100,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl,
            releaseDate = Utils.formatDate(trackDto.releaseDate),
            trackTime = Utils.millisToSeconds(trackDto.trackTimeMillis),
            isFavorite = trackDto.isFavorite
        )
    }
}