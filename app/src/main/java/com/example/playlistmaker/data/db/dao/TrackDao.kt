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

}