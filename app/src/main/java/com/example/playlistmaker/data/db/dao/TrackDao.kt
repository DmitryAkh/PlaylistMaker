package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("Delete FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String?)

    @Query("Select * FROM track_table WHERE isFavorite = 1 ORDER BY additionTime DESC")
    fun getFavTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTrackIdList(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM track_table WHERE trackId = :trackId AND isFavorite = 0)")
    suspend fun existsByTrackIdAndIsFavorite(trackId: String?): Boolean

    @Query("UPDATE track_table SET isFavorite = 0 WHERE trackId = :trackId")
    suspend fun unFavorite(trackId: String?)

    @Query("DELETE FROM track_table WHERE trackId IN (:tracks) AND isFavorite = 0")
    suspend fun deleteTracksByList(tracks: List<String>)
}