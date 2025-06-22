package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TracksInPlCrossRefEntity

@Dao
interface TracksInPlCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(track: TracksInPlCrossRefEntity)

    @Query("DELETE FROM trackInPlCrossRef_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: String?)

    @Query("SELECT EXISTS(SELECT 1 FROM trackInPlCrossRef_table WHERE trackId = :trackId)")
    suspend fun existsByTrackId(trackId: String?): Boolean

    @Query("DELETE FROM trackInPlCrossRef_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

}