package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.dao.TracksInPlCrossRefDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.entity.TracksInPlCrossRefEntity

@Database(
    version = 5,
    entities = [TrackEntity::class, PlaylistEntity::class, TracksInPlCrossRefEntity::class]
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun tracksInPlCrossRefDao(): TracksInPlCrossRefDao
}
