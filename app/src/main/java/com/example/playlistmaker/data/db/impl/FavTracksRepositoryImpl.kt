package com.example.playlistmaker.data.db.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.converters.DbConverter
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.impl.SearchRepositoryImpl.Companion.TRACK_FOR_PLAYER_KEY
import com.example.playlistmaker.data.models.TrackDto
import com.example.playlistmaker.domain.db.FavTracksRepository
import com.example.playlistmaker.domain.entity.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import androidx.core.content.edit
import kotlinx.coroutines.flow.map

class FavTracksRepositoryImpl(
    private val appDataBase: AppDataBase, private val sharedPrefs: SharedPreferences,
) : FavTracksRepository {
    override suspend fun insertFavTrack(track: TrackEntity) {
        appDataBase.trackDao().insertTrack(track)
    }

    override suspend fun deleteFavTrack(trackId: String?) {
        val existsInPlaylists = appDataBase.tracksInPlCrossRefDao().existsByTrackId(trackId)
        if (existsInPlaylists) {
            appDataBase.trackDao().deleteTrack(trackId)
        } else {
            appDataBase.trackDao().unFavorite(trackId)
        }
    }

    override fun getFavTracksFlow(): Flow<List<TrackDto>> =
        appDataBase.trackDao()
            .getFavTracks()
            .map { entities -> convertFromTrackEntity(entities) }


    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackDto> {
        return tracks.map { track -> DbConverter.map(track) }
    }

    override fun putTrackForPlayer(track: Track) {
        sharedPrefs.edit {
            putString(
                TRACK_FOR_PLAYER_KEY, jsonFromTrack(track)
            )
        }
    }

    override fun jsonFromTrack(track: Track): String = Gson().toJson(track)


}


