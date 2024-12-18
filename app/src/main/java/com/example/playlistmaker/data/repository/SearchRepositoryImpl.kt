package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.Utils
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.domain.api.SearchRepository
import com.example.playlistmaker.domain.impl.HISTORY_LIST_KEY
import com.example.playlistmaker.domain.models.ResponseState
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson

class SearchRepositoryImpl(
    private val networkClient: RetrofitClient,
    private val sharedPrefs: SharedPreferences,
) : SearchRepository {
    override fun doSearch(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (networkClient.getResponseState() == ResponseState.SUCCESS) {
            return (response as TracksResponse).results.map {
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
        }
        return emptyList()
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