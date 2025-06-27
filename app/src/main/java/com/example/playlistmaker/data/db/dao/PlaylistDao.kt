package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("Delete FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query("Select * FROM playlist_table ORDER BY additionTime DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("UPDATE playlist_table SET tracksIds = :tracks, additionTime = :time, tracksCount = :tracksCount WHERE playlistId = :playlistId")
    suspend fun updatePlaylist(playlistId: Int, tracks: String, time: Long, tracksCount: Int)

    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    fun getOnePlaylist(playlistId: Int?): Flow<PlaylistEntity>

    @Query("UPDATE playlist_table SET playlistName = :name, playlistDescription = :description, coverPath = :coverPath WHERE playlistId = :playlistId")
    suspend fun updatePlaylistData(
        playlistId: Int,
        name: String,
        description: String,
        coverPath: String,
    )

}