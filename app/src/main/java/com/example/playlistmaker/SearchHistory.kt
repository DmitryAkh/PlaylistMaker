package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

const val HISTORY_LIST_KEY = "key_for_history_list"

class SearchHistory(private val sharedPrefs: SharedPreferences) {




    fun addTrackToHistory(track: Track) {

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

    fun clearHistoryList() {
        sharedPrefs.edit()
            .remove(HISTORY_LIST_KEY)
            .apply()
    }

    fun loadHistoryList(): MutableList<Track> {
        val json = sharedPrefs.getString(HISTORY_LIST_KEY, null)
        return if (json != null) {
            historyListFromJson(json)
        } else {
            mutableListOf()
        }
    }


    fun historyListFromJson(json: String?): MutableList<Track> {
        val type = object : com.google.gson.reflect.TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun jsonFromHistoryList(historyList: MutableList<Track>): String {
        return Gson().toJson(historyList)
    }

}