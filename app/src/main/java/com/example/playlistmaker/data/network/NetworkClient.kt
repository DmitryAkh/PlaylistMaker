package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.models.Response
import com.example.playlistmaker.domain.entity.ResponseState

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
    fun getResponseState(): ResponseState
    suspend fun isConnected(): Boolean
}