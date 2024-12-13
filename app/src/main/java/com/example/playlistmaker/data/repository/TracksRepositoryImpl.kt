package com.example.playlistmaker.data.repository

import com.example.playlistmaker.Utils
import com.example.playlistmaker.data.dto.TracksSearchRequest
import com.example.playlistmaker.data.dto.TracksResponse
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: RetrofitClient) : TracksRepository {


    override fun doSearch(expression: String): List<Track> {

        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
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


    override fun getResultCode(): Int {
        return networkClient.getResultCode()
    }


}


