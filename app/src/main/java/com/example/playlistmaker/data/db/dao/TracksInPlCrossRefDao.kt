package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.playlistmaker.data.db.entity.TracksInPlCrossRefEntity

@Dao
interface TracksInPlCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(track: TracksInPlCrossRefEntity)

}