package com.example.playlistmaker.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.data.converters.DbConverter
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.db.entity.TracksInPlCrossRefEntity
import com.example.playlistmaker.data.models.Playlist
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.repositories.PlaylistsRepository
import com.example.playlistmaker.util.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(private val context: Context, private val dataBase: AppDataBase) :
    PlaylistsRepository {

    override fun saveImageToPrivateStorage(uri: Uri): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Playlist Maker")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "cover_${System.currentTimeMillis()}.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.path
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        dataBase.playlistDao().insertPlaylist(DbConverter.map(playlist))
    }

//    override suspend fun getPlaylists(): List<Playlist> {
//        val listEntity = dataBase.playlistDao().getPlaylists()
//        return DbConverter.map(listEntity)
//    }

    override fun getPlaylists(): Flow<List<Playlist>> =
        dataBase.playlistDao()
            .getPlaylists()
            .map { playlists -> DbConverter.map(playlists) }

    override suspend fun addToPlaylist(playlist: Playlist, track: Track) {
        val plId = playlist.playlistId
        val trackId = track.trackId!!
        val additionTime = System.currentTimeMillis()
        dataBase.trackDao().insertTrack(DbConverter.map(track))
        val playlistTrackCrossRef = TracksInPlCrossRefEntity(plId, trackId, additionTime)
        dataBase.tracksInPlCrossRefDao().insertItem(playlistTrackCrossRef)
        val tracks = playlist.tracksIds
        tracks.add(track)
        val tracksCount = tracks.size
        dataBase.playlistDao().updatePlaylist(
            plId,
            Utils.jsonFromList(tracks),
            System.currentTimeMillis(),
            tracksCount
        )
    }
}


