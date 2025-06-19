package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int = 0,
    val playlistName: String,
    val playlistDescription: String,
    val coverPath: String,
    val tracksIds: String = "",
    val tracksCount: Int = 0,
    val additionTime: Long = System.currentTimeMillis(),
)