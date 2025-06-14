package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("Delete FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query("Select * FROM playlist_table ORDER BY additionTime DESC")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("Select tracksIds FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getPlaylistTracksIds(playlistId: Int): String

    @Query("UPDATE playlist_table SET tracksIds = :tracks, additionTime = :time, tracksCount = :tracksCount WHERE playlistId = :playlistId")
    suspend fun updatePlaylist(playlistId: Int, tracks: String, time: Long, tracksCount: Int)
}