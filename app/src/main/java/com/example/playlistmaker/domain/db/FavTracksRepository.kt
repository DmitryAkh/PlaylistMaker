package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface FavTracksRepository {

    suspend fun insertFavTrack(track: TrackEntity)
    suspend fun deleteFavTrack(trackId: String?)
    fun getFavTracksFlow(): Flow<List<TrackDto>>
    fun putTrackForPlayer(track: Track)
    fun jsonFromTrack(track: Track): String

}