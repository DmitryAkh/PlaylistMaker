package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.util.Utils
import com.example.playlistmaker.search.data.dto.TracksResponse
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.network.RetrofitClient
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.ResponseState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.models.Resource
import com.google.gson.Gson

const val HISTORY_LIST_KEY = "key_for_history_list"


class SearchRepositoryImpl(
    private val networkClient: RetrofitClient,
    private val sharedPrefs: SharedPreferences,
) : SearchRepository {
    override fun doSearch(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return Resource.Success((response as TracksResponse).results.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    collectionName = it.collectionName,
                    primaryGenreName = it.primaryGenreName,
                    artworkUrl100 = it.artworkUrl100,
                    country = it.country,
                    previewUrl = it.previewUrl,
                    releaseDate = Utils.formatDate(it.releaseDate),
                    trackTime = Utils.formatTrackTime(it.trackTimeMillis)
                )
            }
            )

        }
        return Resource.Error("Ошибка сервера")
    }

    override fun getResponseState(): ResponseState {
        return networkClient.getResponseState()
    }

    override fun addTrackToHistory(track: Track) {
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

    override fun loadHistoryList(): MutableList<Track> {
        val json = sharedPrefs.getString(HISTORY_LIST_KEY, null)
        return if (json != null) {
            historyListFromJson(json)
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

}