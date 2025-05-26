package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.domain.db.FavTracksInteractor
import com.example.playlistmaker.domain.db.FavTracksRepository
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

class FavTracksInteractorImpl(
    private val repository: FavTracksRepository,
) : FavTracksInteractor {
    override suspend fun insertFavTrack(track: TrackEntity) {
        repository.insertFavTrack(track)
    }

    override fun getFavTracks(): Flow<List<TrackDto>> {
        return repository.getFavTracksFlow()
    }

    override suspend fun deleteFavTrack(trackId: String?) {
        repository.deleteFavTrack(trackId)
    }

    override fun putTrackForPlayer(track: Track) =
        repository.putTrackForPlayer(track)


}