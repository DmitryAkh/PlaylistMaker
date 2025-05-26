package com.example.playlistmaker.data.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.converters.TrackConverter
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.models.TracksResponse
import com.example.playlistmaker.data.models.TracksSearchRequest
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.repositories.SearchRepository
import com.example.playlistmaker.domain.entity.ResponseState
import com.example.playlistmaker.domain.entity.Resource
import com.example.playlistmaker.domain.entity.Track
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPrefs: SharedPreferences,
    private val appDataBase: AppDataBase,
) : SearchRepository {
    override fun doSearch(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            val favTracks = appDataBase.trackDao().getFavTrackIdList()
            val foundTracks = (response as TracksResponse).results.map {
                TrackConverter.map(it)
            }
            val checkedTracks = foundTracks.map { track ->
                track.copy(isFavorite = track.trackId in favTracks)
            }

            emit(
                Resource.Success(checkedTracks)
            )
        } else {
            emit(Resource.Error("Ошибка сервера"))
        }
    }

    override fun getResponseState(): ResponseState = networkClient.getResponseState()

    override suspend fun addTrackToHistory(track: Track) {
        val historyList = loadHistoryList()

        if (historyList.contains(track)) {
            historyList.remove(track)
        }
        if (historyList.size >= 10) {
            historyList.removeAt(historyList.size - 1)
        }

        historyList.add(0, track)
        sharedPrefs.edit()
            .putString(
                HISTORY_LIST_KEY, jsonFromHistoryList(historyList)
            )
            .apply()
    }

    override fun clearHistoryList() {
        sharedPrefs.edit()
            .remove(HISTORY_LIST_KEY)
            .apply()
    }

    override suspend fun loadHistoryList(): MutableList<Track> {
        val json = sharedPrefs.getString(HISTORY_LIST_KEY, null)
        return if (json != null) {
            val favTracks = appDataBase.trackDao().getFavTrackIdList()
            val historyList = historyListFromJson(json)
            historyList.map { track -> track.copy(isFavorite = track.trackId in favTracks) }
                .toMutableList()
        } else {
            mutableListOf()
        }
    }

    override fun historyListFromJson(json: String?): MutableList<Track> {
        val type = object : com.google.gson.reflect.TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun jsonFromHistoryList(historyList: MutableList<Track>): String {
        return Gson().toJson(historyList)
    }

    override fun putTrackForPlayer(track: Track) {
        sharedPrefs.edit()
            .putString(
                TRACK_FOR_PLAYER_KEY, jsonFromTrack(track)
            )
            .apply()
    }


    override fun jsonFromTrack(track: Track): String = Gson().toJson(track)

    companion object {
        const val HISTORY_LIST_KEY = "key_for_history_list"
        const val TRACK_FOR_PLAYER_KEY = "key_for_track"
    }

}