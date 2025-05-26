package com.example.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.data.models.Response
import com.example.playlistmaker.domain.entity.ResponseState
import com.example.playlistmaker.data.models.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitClient(private val context: Context, private val iTunesService: ITunesApi) :
    NetworkClient {


    private var responseState: ResponseState = ResponseState.DEFAULT

    override suspend fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest && isConnected()) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = iTunesService.search(dto.searchRequest)

                    if (response.results.isNotEmpty()) {
                        response.apply {
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
            }
        } else {
            return Response().apply {
                resultCode = -1
                responseState = ResponseState.NO_INTERNET
            }
        }
    }

    override fun getResponseState(): ResponseState = responseState

    override suspend fun isConnected(): Boolean {
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
