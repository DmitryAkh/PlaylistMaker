package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") song: String): Call<TracksResponse>

}


