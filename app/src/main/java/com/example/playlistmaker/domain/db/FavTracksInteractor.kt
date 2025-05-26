package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface FavTracksInteractor {

    suspend fun insertFavTrack(track: TrackEntity)
    fun getFavTracks(): Flow<List<TrackDto>>
    suspend fun deleteFavTrack(trackId: String?)
    fun putTrackForPlayer(track: Track)

}