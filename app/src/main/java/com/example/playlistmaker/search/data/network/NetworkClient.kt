package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.domain.ResponseState

interface NetworkClient {
    fun doRequest(dto: Any): Response
    fun getResponseState(): ResponseState
    fun isConnected(): Boolean
}