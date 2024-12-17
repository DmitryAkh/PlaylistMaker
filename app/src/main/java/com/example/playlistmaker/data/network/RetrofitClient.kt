package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.ResponseState
import com.example.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesApi::class.java)
    private var responseState: ResponseState = ResponseState.DEFAULT

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            return try {
                val response = iTunesService.search(dto.searchRequest).execute()

                if (response.isSuccessful && response.body() != null) {
                    response.body()!!.apply {
                        responseState = ResponseState.SUCCESS
                    }
                } else {

                    Response().apply {
                        responseState = ResponseState.NOT_FOUND
                    }
                }

            } catch (e: Exception) {
                Log.e("RetrofitClient", "Ошибка: ${e.localizedMessage}", e)
                Response().apply {
                }
            }
        } else {
            return Response().apply {
                responseState = ResponseState.NO_INTERNET
            }
        }
    }

    override fun getResponseState(): ResponseState {
        return responseState
    }
}
