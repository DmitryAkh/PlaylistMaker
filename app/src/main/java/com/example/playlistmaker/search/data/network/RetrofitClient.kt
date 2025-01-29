package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.domain.ResponseState
import com.example.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitClient(private val context: Context, private val iTunesService: ITunesApi) :
    NetworkClient {


    private var responseState: ResponseState = ResponseState.DEFAULT

    override fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest && isConnected()) {
            return try {
                val response = iTunesService.search(dto.searchRequest).execute()
                val body = response.body()

                if (!body?.results.isNullOrEmpty()) {
                    response.body()!!.apply {
                        resultCode = 200
                        responseState = ResponseState.SUCCESS
                    }
                } else {

                    Response().apply {
                        resultCode = 404
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
                resultCode = -1
                responseState = ResponseState.NO_INTERNET
            }
        }
    }

    override fun getResponseState(): ResponseState = responseState

    override fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
